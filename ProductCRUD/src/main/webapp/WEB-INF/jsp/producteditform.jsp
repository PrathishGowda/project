<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
  
        <h1>Edit Employee</h1>  
       <form:form method="POST" action="/ProductCRUD/editsave">    
        <table >    
        <tr>  
        <td></td>    
         <td><form:hidden  path="pid" /></td>  
         </tr>   
         <tr>    
          <td>Name : </td>   
          <td><form:input path="pname"  /></td>  
         </tr>    
         <tr>    
          <td>Price :</td>    
          <td><form:input path="pprice" /></td>  
         </tr>   
         <tr>    
          <td>Quantity :</td>    
          <td><form:input path="pqty" /></td>  
         </tr>   
           
         <tr>    
          <td> </td>    
          <td><input type="submit" value="editSave" /></td>    
         </tr>    
        </table>    
       </form:form>    