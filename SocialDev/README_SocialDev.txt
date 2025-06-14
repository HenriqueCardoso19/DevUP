
# Social Dev - Projeto Java

## Descrição Rápida
Social Dev é um sistema orientado a objetos modelado em Java para simular um ambiente de desenvolvimento de software colaborativo, com base em metodologias ágeis como Scrum e Kanban.

O projeto estrutura diversas classes representando papéis e componentes do desenvolvimento de software:
- **Usuário e subclasses**: Papéis como Gerente, Desenvolvedor, Testador, etc.
- **Projeto e Equipe**: Organização e alocação de times.
- **Métodos Ágeis**: Implementação de métodos como Scrum e Kanban.
- **Relatórios, Agenda, Artefatos**: Recursos utilizados no fluxo de trabalho.

## Código Pensado
A estrutura foca em reutilização e separação de responsabilidades:
- **Herança**: Usuário é a superclasse para todos os papéis.
- **Polimorfismo**: Métodos ágeis compartilham interface comum.
- **Composição**: Classes como Projeto e Equipe agregam objetos.

O ponto de entrada (`Main.java`) é um esqueleto para iniciar a interface via terminal.

Este projeto é ideal para apresentação de conceitos de orientação a objetos, modelagem de sistemas e introdução à engenharia de software ágil.
