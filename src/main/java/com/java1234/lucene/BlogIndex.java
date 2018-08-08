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

//����������
public class BlogIndex {

	private Directory dir;
	
	//��ȡindexWriterʵ��,(��װ�õĹ�����)
	private IndexWriter getWriter()throws Exception{
		dir=FSDirectory.open(Paths.get("D://lucene"));
		SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();//����һ��������
		IndexWriterConfig iwc=new IndexWriterConfig(analyzer);//�õ����ã�����ʵ����IndexWriter
		IndexWriter writer=new IndexWriter(dir,iwc);
		return writer;
	}
	
	//���Ӳ�������
	public void addIndex(Blog blog)throws Exception{
		IndexWriter writer=getWriter();
		Document doc=new Document();//ʵ����һ���ĵ�����
		doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
		doc.add(new TextField("title",blog.getTitle(),Field.Store.YES));
		doc.add(new StringField("releaseDate",DateUtil.formatDate(new Date(), "yyyy-MM-dd"),Field.Store.YES));
		doc.add(new TextField("content",blog.getContentNoTag(),Field.Store.YES));
		writer.addDocument(doc);
		writer.close();
	}
	
	//ɾ������
	public void deleteIndex(String blogId)throws Exception{
		IndexWriter writer=getWriter();
		writer.deleteDocuments(new Term("id",blogId));//�ĵ�����id������ԣ�ͨ��id���������ɾ��
		writer.forceMergeDeletes();//�ϲ�����Ƭ��ǿ��ɾ������Ȼ����ɾ��
		writer.commit();//�ύ
		writer.close();//��ʱ�رգ���Ȼ������ڴ�й©
	}
	
	//��������
	public void updateIndex(Blog blog)throws Exception{
		IndexWriter writer=getWriter();
		Document doc=new Document();//ʵ����һ���ĵ�����
		doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
		doc.add(new TextField("title",blog.getTitle(),Field.Store.YES));
		doc.add(new StringField("releaseDate",DateUtil.formatDate(new Date(), "yyyy-MM-dd"),Field.Store.YES));
		doc.add(new TextField("content",blog.getContentNoTag(),Field.Store.YES));
		writer.updateDocument(new Term("id",String.valueOf(blog.getId())), doc);
		writer.close();
	}
	
	//��ѯ������Ϣ,������鵽�Ķ�����ǰ���ӹ������ļ�¼���������д���Lucene�ִ�ģ��֮ǰ��������ݶ���û�����ӹ������ģ���Щ��¼�����޷��������
	public List<Blog> searchBlog(String q)throws Exception{
		dir=FSDirectory.open(Paths.get("D://lucene"));
		IndexReader reader=DirectoryReader.open(dir);//���reader����
		IndexSearcher is=new IndexSearcher(reader);
		BooleanQuery.Builder booleanQuery=new BooleanQuery.Builder();
		SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
		//��������ѯ�����Լ������query
		QueryParser parser=new QueryParser("title",analyzer);
		Query query=parser.parse(q);
		
		QueryParser parser2=new QueryParser("content",analyzer);
		Query query2=parser2.parse(q);
		
		//�����ѯ�ǻ�Ĺ�ϵ,����ʹ�õ���should
		booleanQuery.add(query, BooleanClause.Occur.SHOULD);
		booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
		//����Ƕ�������ѯ��ֻ��ʾ��ǰ100������,��100����������ѵ÷ֵ�
		TopDocs hits=is.search(booleanQuery.build(), 100);
		
		QueryScorer scorer=new QueryScorer(query);//���ݱ���ƥ��̶�������÷�
		Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);
		SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");//ǰ��Ҫ�Ӵ���ͺ�ɫ
		Highlighter highlighter=new Highlighter(simpleHTMLFormatter,scorer);
		highlighter.setTextFragmenter(fragmenter);
		
		List<Blog> blogList=new LinkedList<Blog>();
		for(ScoreDoc scoreDoc:hits.scoreDocs) {
			Document doc=is.doc(scoreDoc.doc);
			Blog blog=new Blog();
			blog.setId(Integer.parseInt(doc.get("id")));
			blog.setReleaseDateStr(doc.get("releaseDate"));
			String title=doc.get("title");
			String content=StringEscapeUtils.escapeHtml(doc.get("content"));//��content�����һЩ��ǩ���й��ˣ���ֹ��Щ��ǩ��ҳ���ϵı�ǩ������ͻ�����õ���Apache�Ŀ�Դ��StringEscapeUtils
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
					if(content.length()<=200) {//���content�ܳ��ͽ�ȡǰ200���֣������ԭ�����
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