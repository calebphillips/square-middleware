# square-middleware

Implementation of the problem described here https://squareup.com/jobs/ohExVfw7

Includes an HTTP service:

* Is available only over HTTPS
* Requires Basic Authentication (hard coded username/password = fred/flintstone)
* Responds to requests matching **/locations/:location_id** using PUT, GET or DELETE
* Generates responses with random status codes from this list http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html and includes the location_id in the body
* Uses a Ring middleware to log the moving average and variance of the returned status codes to standard out.

## Usage

### Server

* Run ./server.sh
* When prompted for keystore password, enter 'password'

### Client

I used curl to access the service:

```bash
curl -I -X PUT --insecure --user fred:flintstone https://localhost:8443/locations/123
```

I have also included scripts for common cases:

```bash
sample-request.sh
unauthed-request.sh
unsupported-method-request.sh
```

## License

Copyright (C) 2012 Caleb Phillips

Distributed under the Eclipse Public License, the same as Clojure.
