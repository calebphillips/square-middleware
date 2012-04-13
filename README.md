# square-middleware

Implementation of the problem described here https://squareup.com/jobs/ohExVfw7

Includes an HTTP service:

* Is available only over HTTPS
* Requires Basic Authentication (hard coded username/password = fred/flintstone)
* Responds to requests matching **/locations/:location_id** for any HTTP verb
* Generates responses with random status codes from this list http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html and includes the location_id in the body

## Usage

### Server

* Run ./server.sh
* When prompted for keystore password, enter 'password'

### Client

I used curl to access the service:

```bash
curl -I -X PUT --insecure --user fred:flintstone https://localhost:8443/locations/123
```

## License

Copyright (C) 2012 Caleb Phillips

Distributed under the Eclipse Public License, the same as Clojure.
