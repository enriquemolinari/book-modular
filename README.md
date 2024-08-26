# Cinema Ticket System

## Modular Monolith Architecture Style

- `git clone https://github.com/enriquemolinari/book-modular.git`
- `cd book-modular`
- To compile and install all dependencies: `mvn install`
- To run all tests: `mvn test`
- To start the application: `mvn exec:java`. It will start SpringBoot and set up every module. A sample movie data is
  loaded at startup.
    - Once started, you can open swagger UI:
    - http://localhost:8080/swagger-ui/index.html
    - For the `/login` endpoint, some sample users:
        - nico/123456789012
        - lucia/123456789012
        - jsimini/123456789012
        - emolinari/123456789012
    - After successfull login, Swagger will handle the authentication Http Only cookie transparently.
