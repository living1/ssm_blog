package com.java1234.lucene;

import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.java1234.entity.Blog;
import com.java1234.util.DateUtil;
import com.java1234.util.StringUtil;

//博客索引类
public class BlogIndex {

	private Directory dir;
	
	//获取indexWriter实例,(封装好的工具类)
	private IndexWriter getWriter()throws Exception{
		dir=FSDirectory.open(Paths.get("D://lucene"));
		SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();//定义一个分析器
		IndexWriterConfig iwc=new IndexWriterConfig(analyzer);//得到配置，用于实例化IndexWriter
		IndexWriter writer=new IndexWriter(dir,iwc);
		return writer;
	}
	
	//添加博客索引
	public void addIndex(Blog blog)throws Exception{
		IndexWriter writer=getWriter();
		Document doc=new Document();//实例化一个文档对象
		doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
		doc.add(new TextField("title",blog.getTitle(),Field.Store.YES));
		doc.add(new StringField("releaseDate",DateUtil.formatDate(new Date(), "yyyy-MM-dd"),Field.Store.YES));
		doc.add(new TextField("content",blog.getContentNoTag(),Field.Store.YES));
		writer.addDocument(doc);
		writer.close();
	}
	
	//删除索引
	public void deleteIndex(String blogId)throws Exception{
		IndexWriter writer=getWriter();
		writer.deleteDocuments(new Term("id",blogId));//文档中有id这个属性，通过id这个属性来删除
		writer.forceMergeDeletes();//合并索引片，强制删除，不然不会删除
		writer.commit();//提交
		writer.close();//及时关闭，不然会造成内存泄漏
	}
	
	//更新索引
	public void updateIndex(Blog blog)throws Exception{
		IndexWriter writer=getWriter();
		Document doc=new Document();//实例化一个文档对象
		doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
		doc.add(new TextField("title",blog.getTitle(),Field.Store.YES));
		doc.add(new StringField("releaseDate",DateUtil.formatDate(new Date(), "yyyy-MM-dd"),Field.Store.YES));
		doc.add(new TextField("content",blog.getContentNoTag(),Field.Store.YES));
		writer.updateDocument(new Term("id",String.valueOf(blog.getId())), doc);
		writer.close();
	}
	
	//查询博客信息,这里面查到的都是提前添加过索引的记录，如果是在写这个Lucene分词模块之前导入的数据都是没有添加过索引的，这些记录都是无法查出来的
	public List<Blog> searchBlog(String q)throws Exception{
		dir=FSDirectory.open(Paths.get("D://lucene"));
		IndexReader reader=DirectoryReader.open(dir);//获得reader对象
		IndexSearcher is=new IndexSearcher(reader);
		BooleanQuery.Builder booleanQuery=new BooleanQuery.Builder();
		SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
		//多条件查询，所以简历多个query
		QueryParser parser=new QueryParser("title",analyzer);
		Query query=parser.parse(q);
		
		QueryParser parser2=new QueryParser("content",analyzer);
		Query query2=parser2.parse(q);
		
		//这个查询是或的关系,所以使用的是should
		booleanQuery.add(query, BooleanClause.Occur.SHOULD);
		booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
		//这个是多条件查询，只显示出前100条数据,这100条数据是最佳得分的
		TopDocs hits=is.search(booleanQuery.build(), 100);
		
		QueryScorer scorer=new QueryScorer(query);//根据标题匹配程度来计算得分
		Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);
		SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");//前后都要加粗体和红色
		Highlighter highlighter=new Highlighter(simpleHTMLFormatter,scorer);
		highlighter.setTextFragmenter(fragmenter);
		
		List<Blog> blogList=new LinkedList<Blog>();
		for(ScoreDoc scoreDoc:hits.scoreDocs) {
			Document doc=is.doc(scoreDoc.doc);
			Blog blog=new Blog();
			blog.setId(Integer.parseInt(doc.get("id")));
			blog.setReleaseDateStr(doc.get("releaseDate"));
			String title=doc.get("title");
			String content=StringEscapeUtils.escapeHtml(doc.get("content"));//对content里面的一些标签进行过滤，防止这些标签与页面上的标签发生冲突。采用的是Apache的开源包StringEscapeUtils
			if(title!=null) {
				TokenStream tokenStream=analyzer.tokenStream("title", new StringReader(title));
				String hTitle=highlighter.getBestFragment(tokenStream, title);
				if(StringUtil.isEmpty(hTitle)) {
					blog.setTitle(title);
				}else {
					blog.setTitle(hTitle);
				}
			}
			
			if(content!=null) {
				TokenStream tokenStream=analyzer.tokenStream("content", new StringReader(content));
				String hContent=highlighter.getBestFragment(tokenStream, content);
				if(StringUtil.isEmpty(hContent)) {
					if(content.length()<=200) {//如果content很长就截取前200个字，否则就原样输出
						blog.setContent(content);
					}else {
						blog.setContent(content.substring(0, 200));
					}
				}else {
					blog.setContent(hContent);
				}
			}
			blogList.add(blog);
		}
		return blogList;
	}
}
