package danvith;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller    
public class ProductController {    
    @Autowired    
    ProductDao dao;//will inject dao from XML file    
        
    public ProductDao getDao() {
		return dao;
	}
	public void setDao(ProductDao dao) {
		this.dao = dao;
	}
	/*It displays a form to input data, here "command" is a reserved request attribute  
     *which is used to display object data into form  
     */    
    @RequestMapping("/productform")    
    public String showform(Model m){    
        m.addAttribute("command", new Product());  
        return "productform";   
    }    
    /*It saves object into database. The @ModelAttribute puts request data  
     *  into model object. You need to mention RequestMethod.POST method   
     *  because default request is GET*/    
    @RequestMapping(value="/save",method = RequestMethod.POST)    
    public String save(@ModelAttribute("Product") Product product){    
        dao.save(product);    
        return "redirect:/viewproduct";//will redirect to viewemp request mapping    
    }    
    /* It provides list of products in model object */    
    @RequestMapping("/viewproduct")    
    public String viewproduct(Model m){    
        List<Product> list=dao.getProducts();    
        m.addAttribute("list",list);  
        return "viewproduct";    
    }    
    /* It displays object data into form for the given id.   
     * The @PathVariable puts URL data into variable.*/    
    @RequestMapping(value="/editproduct/{id}")    
    public String edit(@PathVariable int id, Model m){    
    	Product product=dao.getProductById(id);    
        m.addAttribute("command",product);  
        return "producteditform";    
    }    
    /* It updates model object. */    
    @RequestMapping(value="/editsave",method = RequestMethod.POST)    
    public String editsave(@ModelAttribute("product") Product product){    
        dao.update(product); 
        return "redirect:/viewproduct";    
    }    
    /* It deletes record for the given id in URL and redir*/
    @RequestMapping(value="/deleteproduct",method = RequestMethod.GET)    
    public String delete(HttpServletRequest request){
    	int id=Integer.parseInt(request.getParameter("pid"));//id in string formate to convert into int type using parseInt()
        dao.delete(id);    
        return "redirect:/viewproduct";    
    }     
}  
