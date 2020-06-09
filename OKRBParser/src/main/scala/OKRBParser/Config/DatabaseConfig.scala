package OKRBParser.Config

case class DatabaseConfig(driver:String,
                          url:String,
                          user:String,
                          password:String,
                          poolSize:Int)
