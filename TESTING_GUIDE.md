# Guia de Testes do Projeto BookMarketCore

Este documento descreve a filosofia e a estratégia de testes para o projeto BookMarketCore. O objetivo é garantir que todas as funcionalidades sejam robustas, bem documentadas e correspondam aos requisitos de negócio.

## Filosofia: Behavior-Driven Development (BDD)

Adotamos uma abordagem de **Desenvolvimento Guiado por Comportamento (BDD)**. Isso significa que o desenvolvimento de qualquer nova funcionalidade deve começar pela escrita de um teste.

O teste não é apenas uma ferramenta de verificação; ele é a **especificação executável** do comportamento esperado do sistema. Cada teste deve descrever um cenário de negócio de forma clara e legível.

**O ciclo de desenvolvimento é:**
1.  **Red (Vermelho):** Escreva um novo teste em `BookmarketTest.java` que descreva um comportamento esperado (um critério de aceite de uma User Story). Execute o teste e veja-o falhar.
2.  **Green (Verde):** Escreva o código de produção mínimo necessário em `Bookmarket.java` para fazer o teste passar.
3.  **Refactor (Refatorar):** Com o teste passando, melhore a qualidade e a clareza do código de produção sem alterar seu comportamento.

## Estrutura dos Testes

-   **`src/test/java/servico/BookstoreTest.java`**: Testa a camada de acesso a dados (o "repositório" em memória). Garante que a criação e recuperação de dados básicos estão funcionando. Novos testes aqui só são necessários se a classe `Bookstore.java` ganhar novas funcionalidades de acesso a dados.

-   **`src/test/java/servico/BookmarketTest.java`**: **Este é o local principal para os testes de negócio.** Cada método de teste nesta classe deve validar um cenário específico de uma User Story.

## Como Escrever Novos Testes

1.  **Fonte da Verdade:** O arquivo `docs/board.pdf` é a fonte da verdade para todos os cenários de teste, tanto positivos quanto negativos.

2.  **Nome do Teste:** O nome do método de teste deve ser descritivo, seguindo o padrão `test<Funcionalidade>_<Cenario>()`.
    *   **Bom:** `testRecommendation_ForNewCustomer_ShouldReturnBestsellers()`
    *   **Ruim:** `testRecs1()`

3.  **Estrutura do Teste (Arrange-Act-Assert):**
    *   **Arrange (Arranjar):** Prepare o ambiente para o seu teste. O método `setUp` já cria um ambiente rico, mas você pode precisar criar dados específicos, como um cliente novo.
    *   **Act (Agir):** Execute o método da lógica de negócio que você está testando (ex: `Bookmarket.getPriceBookRecommendationByUsers(...)`).
    *   **Assert (Afirmar):** Verifique se o resultado da ação corresponde ao comportamento esperado. Use os métodos de `org.junit.Assert` (`assertEquals`, `assertTrue`, `assertThrows`, etc.).

### Exemplo de Caso de Teste (da US1)

**User Story:** "Como usuário, quero solicitar os N livros mais vendidos."

**Caso de Teste (No2):** "Limite Inválido: N igual a zero deve retornar um erro de validação."

**Implementação em `BookmarketTest.java`:**
```java
@Test
public void testGetBestsellers_WithInvalidLimitOfZero_ShouldThrowException() {
    // Arrange
    int invalidLimit = 0;

    // Act & Assert
    // Verifica se a execução do método com o limite inválido
    // realmente lança a exceção esperada.
    assertThrows(IllegalArgumentException.class, () -> {
        Bookmarket.getBestSellers(invalidLimit);
    });
}
```
Seguindo este guia, garantimos que o projeto evolua de forma consistente, com uma cobertura de testes que reflete diretamente as regras de negócio acordadas.
