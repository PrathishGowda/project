<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
  
        <h1>Add New Product</h1>  
       <form:form method="post" action="save">    
        <table >    
        <tr>    
          <td>Product Id : </td>   
          <td><form:input path="pid"  /></td>  
         </tr> 
         <tr>    
          <td>Product Name : </td>   
          <td><form:input path="pname"  /></td>  
         </tr>    
         <tr>    
          <td>Product Price :</td>    
          <td><form:input path="pprice" /></td>  
         </tr>   
         <tr>    
          <td>Quantity :</td>    
          <td><form:input path="pqty" /></td>  
         </tr>   
         <tr>    
          <td> </td>    
          <td><input type="submit" value="Save" /></td>    
         </tr>    
        </table>    
       </form:form>   