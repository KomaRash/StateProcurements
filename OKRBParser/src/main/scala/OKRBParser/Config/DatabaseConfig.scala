package OKRBParser.Config

case class DatabaseConfig(driver:String,
                          url:String,
                          password:String,
                          user:String,
                          poolSize:Int)
