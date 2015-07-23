# BacksideServlet
A JSON WebServices platform which manages user accounts and provides TopicMap services

## Usage ##<br/>

1- Boot ElasticSearch (make certain that /config/jsondocstore-props.xml properly points to ElasticSearch<br/>
2- Boot BacksideServlet with run.bat or run.sh

**Note (20150723)**: System is now wired to ElasticSearch running on localhost:9300 (tcp port). Many bug fixes today.<br/>
**Note (20150719)**: Very early stage development. As coded, the system can handle user accounts with an embedded H2 database. It will soon handle a webservices-based topic map API for content management.