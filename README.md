# square-middleware

Implementation of the problem described here https://squareup.com/jobs/ohExVfw7

Includes an HTTP service:
* Is available on over HTTPS
* Requires Basic Authentication
* Responds to request matching /locations/:location_id for any HTTP verb
* Generates simple responses with random status codes from this list http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html and includes the location_id in the body

## Usage

FIXME: write

## License

Copyright (C) 2012 Caleb Phillips

Distributed under the Eclipse Public License, the same as Clojure.
