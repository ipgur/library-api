A read-only REST API that sources the MySQL database behind strumski.com and provides read-access to books and authors.

Demo available at: http://strumskiapi-env.ptjyutvivi.eu-central-1.elasticbeanstalk.com/

- Uses undertow as servlet container.
- Supports retrieving credentials from AWS KMS
- Integrates Swagger UI
- Supports reporting to cloud watch
- Provides two sample VUE.js pages that use the API to display books and authors


                    

Name     | Status |
-------- | ------ |
Build    | [![CircleCI](https://circleci.com/gh/ipgur/library-api.svg?style=svg)](https://circleci.com/gh/ipgur/library-api) |