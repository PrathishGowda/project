<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<html>
<body>
<h1>Products List</h1>  
<table border="2" width="70%" cellpadding="2">  
<tr><th>Id</th><th>Name</th><th>Price</th><th>Quantity</th><th>Edit</th><th>Delete</th></tr>  
   <c:forEach var="product" items="${list}">   
		   <tr>  
		   <td>${product.pid}</td>  
		   <td>${product.pname}</td>  
		   <td>${product.pprice}</td>  
		   <td>${product.pqty}</td>  
		   <td><a href="editproduct/${product.pid}">Edit</a></td>  
		   <td><a href="deleteproduct?pid=${product.pid}">Delete</a></td>  
		   </tr>  
   </c:forEach>  
</table>  
   <br/>  
   <a href="productform">Add New Product</a> 
</body> 
</html>