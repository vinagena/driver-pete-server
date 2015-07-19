# driver-pete-server
Server for driver pete

To run locally, create application.properties in src/main/resources with the following content:

server.port=8443
server.ssl.key-store = src/test/resources/test_keystore
server.ssl.key-store-password = temppwd

fb.login_form_host=www.facebook.com
fb.api_host=graph.facebook.com
fb.client_id=<YOUR_APP_ID>
fb.secret=<YOUR_APP_SECRET>
fb.redirect_uri=https://localhost:8443/signin/facebook
fb.use_safe_https=true
