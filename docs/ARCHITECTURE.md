# Documento de Arquitetura - BookMarketCore

Este documento detalha a arquitetura de software para o sistema de recomendação BookMarketCore.

## 1. Visão Geral e Princípios

A arquitetura do BookMarketCore é baseada em uma abordagem de **Domain-Driven Design (DDD) simplificada**, com uma clara separação entre a camada de domínio, serviço e persistência (através de interfaces).

Os princípios que guiam o design são:
- **Alta Coesão e Baixo Acoplamento:** Cada componente tem uma responsabilidade única e bem definida. As dependências entre os componentes são minimizadas pelo uso de interfaces.
- **Preparação para o Futuro:** A arquitetura, através do uso de interfaces de repositório, é projetada para evoluir. A implementação atual em memória (`Bookstore`) pode ser substituída por uma implementação de banco de dados no futuro sem impacto na camada de negócio.
- **Centralização da Lógica de Negócio:** A lógica de negócio é orquestrada na camada de serviço (`Bookmarket`), que atua como uma fachada, consumindo dados dos repositórios e utilizando outros serviços especializados.

## 2. Diagrama de Componentes

*(Um diagrama de componentes será gerado em `docs/diagrams/COMPONENT_DIAGRAM.puml` para ilustrar visualmente esta seção)*

Os principais componentes do sistema são:

### 2.1. Pacote `dominio`
- **Responsabilidade:** Contém as entidades e objetos de valor do negócio (POJOs). Não possui lógica de negócio complexa nem dependências de frameworks.
- **Classes Principais:** `Book`, `Customer`, `Order`, `OrderLine`, `Evaluation`.

### 2.2. Pacote `repositorio` (Interfaces)
- **Responsabilidade:** Define os "contratos" para a camada de persistência. Abstrai como os dados são armazenados e recuperados.
- **Interfaces Principais:** `BookRepository`, `OrderRepository`.

### 2.3. Pacote `servico`
- **Responsabilidade:** Contém a implementação da lógica de negócio e a orquestração dos componentes.
- **Classes Principais:**
    - **`Bookmarket.java`**: A **Fachada de Serviço (Service Facade)**. É o ponto de entrada público para todas as operações de negócio (User Stories). Ele consome os repositórios e outros serviços para executar suas tarefas.
    - **`Bookstore.java`**: A **Implementação do Repositório em Memória**. Atualmente, esta classe atua como o banco de dados da aplicação. (TODO: No futuro, esta classe deve ser refatorada para implementar as interfaces do pacote `repositorio`).
    - **`RecommendationService.java`**: Um **Serviço Especializado** para a lógica de recomendação. Ele encapsula a complexidade da biblioteca Apache Mahout, expondo um método simples para obter recomendações de livros.

## 3. Fluxo de Dados: Caso de Uso de Recomendação (US3 & US4)

*(Um diagrama de sequência será gerado em `docs/diagrams/SEQUENCE_DIAGRAM_RECOMMENDATION.puml` para ilustrar este fluxo)*

O fluxo para obter uma recomendação de preço para um cliente (`getPriceBookRecommendationByUsers`) é o seguinte:

1.  **Entrada:** Uma chamada externa (ex: de um cliente API) chega ao método `Bookmarket.getPriceBookRecommendationByUsers(c_id)`.
2.  **Busca de Cliente:** O `Bookmarket` (idealmente através de um `CustomerRepository`) obtém os dados do cliente para verificar se ele é um assinante (analisando o campo `discount`).
3.  **Obter Recomendações:** O `Bookmarket` solicita ao `RecommendationService` uma lista de livros recomendados para aquele cliente (`getRecommendations(c_id, 5)`).
    - O `RecommendationService` usa o `DataModel` do Mahout (previamente populado com as avaliações) para gerar a lista de IDs de livros e a retorna.
4.  **Calcular Preços (Lógica de Negócio):** O `Bookmarket` recebe a lista de livros "crus".
    - **Se for Assinante (US4):** Para cada livro, ele busca o `Stock` correspondente (via `BookRepository`) e obtém o preço atual (`cost`).
    - **Se for Cliente Regular (US3):** Para cada livro, ele busca no histórico de pedidos (via `OrderRepository.findAll()`) todas as `OrderLine`s daquele livro para calcular o preço médio de venda.
5.  **Montagem e Retorno:** O `Bookmarket` monta o `Map<Book, Double>` com os livros e seus preços calculados e o retorna ao chamador.

## 4. Decisões de Design

1.  **Criação do `RecommendationService`:** A lógica do Mahout foi isolada para reduzir a complexidade da fachada `Bookmarket` e seguir o Princípio da Responsabilidade Única.
2.  **Introdução das Interfaces de Repositório:** Para desacoplar a camada de negócio da implementação de persistência em memória, permitindo a evolução futura para um banco de dados real sem a necessidade de reescrever a lógica de negócio.
3.  **Modificação de `OrderLine`:** A adição do campo `price` em `OrderLine` foi uma decisão crítica para viabilizar o requisito de cálculo de preço médio histórico (US3), que não era suportado pelo modelo de dados original.
