importjava.util.regex.*;

public class XSIO {
          public String xsio(String content){
              String pat = "position[^a-zA-Z0-9]{0,10}:[^a-zA-Z0-9]{0,10}absolute";
              String result = content;
              Pattern p = Pattern.compile(pat,Pattern.CASE_INSENSITIVE);
              int i = 0;
              while(true){
                  Matcher m = p.matcher(result);
                  if(!m.find()){
                      break;
                  }
                  i++;
                  if(i <= 5){
                      result = m.replaceAll("");
                  }else{
                      result = "error";
                  }
              }
              return result;
          }
      }
