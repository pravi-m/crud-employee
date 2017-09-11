import javax.inject.Inject

import play.api.http.DefaultHttpFilters
import play.filters.cors.CORSFilter

/**
  * Created by Medi on 29-08-2017.
  */
class Filters @Inject() (corsFilter: CORSFilter)
  extends DefaultHttpFilters(corsFilter){

}
