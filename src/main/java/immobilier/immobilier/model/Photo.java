package immobilier.immobilier.model;
import immobilier.immobilier.annotation.ColumnField; 
import immobilier.immobilier.annotation.Getter; 
import immobilier.immobilier.annotation.Setter; 
import immobilier.immobilier.annotation.TableName; 
import immobilier.immobilier.table.JDBC; 
import java.sql.Connection; 


@TableName(database = "immobilier", driver = "postgres", name = "photo", password = "366325", user = "postgres")
public class Photo extends JDBC {

    @ColumnField(columnName = "id" ,primaryKey = true, defaultValue = true ) 
String id;
    @ColumnField(columnName = "idbiens" ) 
String idbiens;
    @ColumnField(columnName = "nom" ) 
String nom;
    

    public Photo() throws Exception{

    }

    public Photo(String idbiens ,String nom)throws Exception{
setIdbiens(idbiens.trim()); 
setNom(nom.trim()); 
}

    	public int count(Connection connection) throws Exception { 
  int count = 0;
 try { 
     count = select(connection).size();
 } catch (Exception e) {
    e.printStackTrace();
}
return count;
 } 

    
    	 @Getter(columnName = "id")
	public String getId() {
      	return this.id; 
	} 

	 @Setter(columnName = "id")
	public void setId(String id) { 
      	this.id=id;
	} 

	 @Getter(columnName = "idbiens")
	public String getIdbiens() {
      	return this.idbiens; 
	} 

	 @Setter(columnName = "idbiens")
	public void setIdbiens(String idbiens) { 
      	this.idbiens=idbiens;
	} 

	 @Getter(columnName = "nom")
	public String getNom() {
      	return this.nom; 
	} 

	 @Setter(columnName = "nom")
	public void setNom(String nom) { 
      	this.nom=nom;
	} 




}
