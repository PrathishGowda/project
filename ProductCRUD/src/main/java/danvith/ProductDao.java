package danvith;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class ProductDao {
	
	JdbcTemplate template;   
	public void setTemplate(JdbcTemplate template) {    
	    this.template = template;    
	}    

	public int save(Product p) {
		
String sql="insert into producttab(pid,pname,pprice,pqty) values("+p.getPid()+",'"+p.getPname()+"',"+p.getPprice()+","+p.getPqty()+")";    
		    return template.update(sql);  
		
	}

	public List<Product> getProducts() {
		
		String sql="select * from producttab";
		RowMapper<Product> rm=new RowMapper<Product>(){
			public Product mapRow(ResultSet st,int row) throws SQLException
			{
				Product p=new Product();
				p.setPid(st.getInt(1));
				p.setPname(st.getString(2));
				p.setPprice(st.getDouble(3));
				p.setPqty(st.getInt(4));
				return p;
			}
		};
		return template.query(sql,rm);
	}

	public Product getProductById(int id) {
		String sql="select * from producttab where pid=?";    
	    return template.queryForObject(sql, new Object[]{id},new BeanPropertyRowMapper<Product>(Product.class));  
	}

	public int update(Product p) {
		String sql="update producttab set pname='"+p.getPname()+"',pprice="+p.getPprice()+",pqty="+p.getPqty()+" where pid="+p.getPid();    
	    return template.update(sql);   
		 
	}

	public int delete(int id) {
		String sql="delete from producttab where pid=?";    
	    return template.update(sql,id);
		
	}

}
