# BookMarketCore - Sistema de Recomendação

Este projeto contém o núcleo de um sistema de recomendação de livros para a plataforma de e-commerce "Bookmarket".

## Visão Geral

O objetivo é fornecer um serviço de recomendação que possa sugerir livros aos usuários com base em diferentes lógicas de negócio, como popularidade (bestsellers) e perfil de consumo.

A arquitetura segue uma abordagem simplificada de **Domain-Driven Design (DDD)**, separando claramente o modelo de domínio das regras de negócio. Atualmente, o sistema opera com um **banco de dados em memória** (`In-Memory Repository`), mas foi projetado de forma a facilitar uma futura transição para uma solução de persistência real.

**Estado Atual do Projeto:** A estrutura base do projeto, incluindo as entidades de domínio e os serviços, está definida. No entanto, **a lógica de negócio principal ainda não foi implementada**. Os métodos nos serviços estão presentes como stubs (esqueletos) e precisam ser preenchidos.

## Arquitetura e Componentes

O código-fonte está organizado em dois pacotes principais dentro de `src/main/java`:

-   `dominio`: Contém todas as entidades do núcleo de negócio (POJOs - Plain Old Java Objects). Estas classes representam os "substantivos" do sistema.
    -   `Book.java`: Representa um livro.
    -   `Customer.java`: Representa um cliente. O campo `discount` é usado para diferenciar Clientes Regulares (discount = 0) de Assinantes (discount > 0).
    -   `Order.java`: Representa um pedido de um cliente.
    -   `OrderLine.java`: Representa um item dentro de um pedido. Armazena a quantidade (`qty`) e o preço (`price`) da venda, essencial para os cálculos de negócio.
    -   `Stock.java`: Representa o item em estoque, contendo o preço de venda atual (`cost`).
    -   Outras: `Author`, `Address`, etc.

-   `servico`: Contém as classes que orquestram a lógica de negócio do sistema.
    -   `Bookstore.java`: Atua como o **banco de dados em memória**. É responsável por popular e armazenar todas as coleções de dados (livros, clientes, pedidos, etc.) durante a execução.
    -   `Bookmarket.java`: É a **fachada de serviço principal** e o ponto de entrada para todas as operações de negócio. É nesta classe que a lógica para as User Stories (Bestsellers, Recomendações) deve ser implementada.

## Próximos Passos (Implementação)

A próxima fase deste projeto é implementar a lógica de negócio dentro dos métodos existentes em `Bookmarket.java`, utilizando os dados fornecidos por `Bookstore.java`.

## Arquitetura Detalhada

Para uma visão mais aprofundada do design técnico, das decisões de arquitetura e dos fluxos de dados, consulte os seguintes documentos na pasta `/docs`:

- **[Documento de Arquitetura](./docs/ARCHITECTURE.md):** Detalha os componentes, suas responsabilidades e os fluxos de dados para os principais casos de uso.
- **[Evolução da Arquitetura](./docs/ARCHITECTURE_EVOLUTION.md):** Compara a arquitetura atual (As-Is) com a futura (To-Be), explicando os benefícios da evolução do design.
- **Diagramas:**
  - [Diagrama de Componentes](./docs/diagrams/COMPONENT_DIAGRAM.puml)
  - [Diagrama de Sequência para Recomendações](./docs/diagrams/SEQUENCE_DIAGRAM_RECOMMENDATION.puml)
