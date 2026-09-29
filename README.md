# 🧮 Calculadora Spring Boot - Testes Unitários e de Integração

Projeto desenvolvido em **Java 21** e **Spring Boot 3** com foco em testes automatizados, utilizando **JUnit 5**, **Mockito**, **Test Slices do Spring Boot** (`@DataJpaTest`, `@WebMvcTest`, `@SpringBootTest`) e métricas de cobertura com **JaCoCo**.

---

## 🏛️ Arquitetura em Camadas

```
com.pucrs.calculadora
├── controller/         # Endpoints REST e GlobalExceptionHandler
├── dto/                # Data Transfer Objects com Bean Validation
├── exception/          # Exceções customizadas da aplicação
├── model/              # Entidades JPA e Enums
├── repository/         # Interfaces Spring Data JPA
├── service/            # Regras de negócio, auditoria e histórico
└── CalculadoraApplication.java
```

---

## 🧪 Estrutura de Testes e Mapeamento dos Conceitos

| Parte | Classe de Teste | Conceitos Demonstrados |
| :--- | :--- | :--- |
| **Parte 1 (JUnit 5)** | `CalculadoraServiceUnitTest` | Padrão AAA, convenção de nomenclatura, `@DisplayName`, `@Test`, `@ParameterizedTest`, `@CsvSource`, `assertEquals`, `assertAll`, `assertThrows`, `assertDoesNotThrow`. |
| **Parte 1 (Ciclo de Vida)** | `CalculadoraServiceLifecycleTest` | `@BeforeAll`, `@BeforeEach`, `@AfterEach`, `@AfterAll`, isolamento de testes. |
| **Parte 2 (Mockito)** | `HistoricoServiceMockitoTest` | `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks`, `when(...).thenReturn(...)`, `verify(..., times())`, `verify(..., never())`, `ArgumentCaptor`. |
| **Parte 3 (Data JPA Slice)** | `HistoricoCalculoRepositoryTest` | `@DataJpaTest`, `TestEntityManager`, queries customizadas, rollback automático. |
| **Parte 3 (Web MVC Slice)** | `CalculadoraControllerTest` | `@WebMvcTest`, `MockMvc`, `@MockBean`, validação de status HTTP (200, 201, 400, 422) e JSON path. |
| **Parte 3 (Integração E2E)** | `CalculadoraIntegrationTest` | `@SpringBootTest(webEnvironment = RANDOM_PORT)`, `@AutoConfigureMockMvc`, fluxo completo ponta a ponta. |
| **Smoke Test** | `CalculadoraApplicationTests` | Verificação do carregamento do `ApplicationContext`. |

---

## 🚀 Como Executar o Projeto

### 1. Pré-requisitos
- Java 21 (JDK)
- Apache Maven 3.8+

### 2. Executar Todos os Testes e Gerar Cobertura (JaCoCo)
```bash
mvn clean verify
```
O relatório HTML de cobertura será gerado em:
`target/site/jacoco/index.html`

### 3. Executar Apenas Testes Unitários
```bash
mvn test
```

### 4. Iniciar a Aplicação
```bash
mvn spring-boot:run
```

- **Interface Web & API**: [`http://localhost:8085`](http://localhost:8085)
- **Console do H2**: [`http://localhost:8085/h2-console`](http://localhost:8085/h2-console) (JDBC URL: `jdbc:h2:mem:calculadoradb`).

---

## 🗺️ Mapa de Portas do Workspace

| Serviço / Aplicação | Porta | Descrição |
| :--- | :--- | :--- |
| **Calculadora** | **8085** | Aplicação da Calculadora + Frontend Interativo + H2 |
| **API Gateway** (`sistema-microservicos`) | **8080** | Gateway de Entrada de Microserviços |
| **Peças Service** (`sistema-microservicos`) | **8081** | Microserviço de Peças |
| **Clientes Service** (`sistema-microservicos`) | **8082** | Microserviço de Clientes |
| **Representantes Service** (`sistema-microservicos`) | **8083** | Microserviço de Representantes |
| **Eureka Discovery Server** | **8761** | Registro e Descoberta de Serviços |
| **Spring Cloud Config Server** | **8888** | Configuração Centralizada |

---

## 📡 Endpoints Disponíveis

- `POST /api/calculadora/calcular`: Recebe JSON com `{ "operacao": "SOMA", "operandoA": 10, "operandoB": 20 }`
- `GET /api/calculadora/somar?a=10&b=20`
- `GET /api/calculadora/subtrair?a=20&b=5`
- `GET /api/calculadora/multiplicar?a=6&b=7`
- `GET /api/calculadora/dividir?a=15&b=3`
- `GET /api/calculadora/potencia?base=2&expoente=8`
- `GET /api/calculadora/raiz?valor=81`
- `GET /api/calculadora/historico`: Lista histórico recente de cálculos
- `GET /api/calculadora/historico?operacao=SOMA`: Filtra histórico por operação
# Contru-o-de-software
