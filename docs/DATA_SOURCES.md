# Public Data Sources for Incidents

Several government agencies publish road traffic or crime incident data through open data portals or APIs:

- **511 Traffic Feeds** – Many U.S. states expose live traffic incident feeds via their [511](https://en.wikipedia.org/wiki/5-1-1) traveler information programs. These feeds typically include road closures and accidents in XML or JSON formats (e.g. the California DOT `api.511.org`).
- **NHTSA Crash Data** – The U.S. National Highway Traffic Safety Administration provides crash statistics such as the [Fatality Analysis Reporting System (FARS)](https://cdan.nhtsa.dot.gov/). Files are available for download in CSV format.
- **City Open Data Portals** – Municipal portals often expose detailed traffic crash and crime events. Examples include [NYC Open Data](https://opendata.cityofnewyork.us/) and [Chicago Data Portal](https://data.cityofchicago.org/), both of which offer REST APIs.
- **FBI Crime Data API** – The FBI publishes nationwide crime statistics through the [Crime Data Explorer](https://cde.ucr.fbi.gov/) API using the National Incident-Based Reporting System (NIBRS).

These datasets can be ingested with the provided `IncidentIngestor` by downloading CSV files or consuming their REST APIs.
