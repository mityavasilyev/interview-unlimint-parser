# interview-unlimint-parser

# How To:

As declared by the task description - to build project run <br/>
`mvn clean install`. Jar executable then can be found in /target folder. <br/>
Just in case - precompiled jar is also included in the project and can be found in 
the `extradata` folder from project root folder

To run jar executable navigate to jar's location and run via cmd: <br/>
```shell
java 
    -jar interview-unlimint-parser-0.0.1-SNAPSHOT.jar
    interview-unlimint.csv
    interview-unlimint.json
```
where ...SNAPSHOT.jar is compiled project and following `csv` and `json` variables are paths
to files that will be processed. (In my case both jar and data files located in the same directory)

#### Logging
If you'd like to see what's happening under the hood during runtime - navigate to application.yml 
in the [src/.../resources](https://github.com/mityavasilyev/interview-unlimint-parser/blob/master/src/main/resources/application.yml) 
folder and change logging option from `off` to `info`

#### Tests
Bunch of tests are in place to ensure proper parsing execution. 
To skip tests during packaging run `mvn clean install -DskipTests`

To test application (dunno why tho) run `mvn test` from project root folder

# Task description and Project walk-through
```text
Тестовое задание Java-разработчик
Входные данные
CSV файл. 
Назначение столбцов: 
  Идентификатор ордера, сумма, валюта, комментарий   

Пример записи:
1,100,USD,оплата заказа
2,123,EUR,оплата заказа

Примечание: все столбцы обязательны

JSON файл.
Пример записи:
{“orderId”:3,”amount”:1.23,”currency”:”USD”,”comment”:”оплата заказа”}
{“orderId”:4,”amount”:1.24,”currency”:”EUR”,”comment”:”оплата заказа”}

Примечание: все поля обязательны
Выходные данные
{“id”:1,“orderId”:1,”amount”:100,”comment”:”оплата заказа”,”filename”:”orders.csv”,”line”:1,”result”:”OK”}
{“id”:2,“orderId”:2,”amount”:123,”comment”:”оплата заказа”,”filename”:”orders.csv”,”line”:2,”result”:”OK”}
{“id”:3,“orderId”:3,”amount”:1.23,”comment”:”оплата заказа”,”filename”:”orders.json”,”line”:1,”result”:”OK”}
{“id”:4,“orderId”:4,”amount”:1.24,”comment”:”оплата заказа”,”filename”:”orders.json”,”line”:2,”result”:”OK”}

id - идентификатор ордера
amount - сумма ордера
currency - валюта суммы ордера
comment - комментарий по ордеру
filename - имя исходного файла
line - номер строки исходного файла
result - результат парсинга записи исходного файла. 
OK - если запись конвертирована корректно, 
или описание ошибки если запись не удалось конвертировать.

Описание задания:
Необходимо разработать приложение парсинга входящих данных и конвертирования результат парсинга в результирующий формат.
Требуется простое решение задания,как если бы приложение могли поддерживать и сопровождать другие менее опытные разработчики 
Приложение должно быть реализовано с использованием фреймворка Spring.
Исходные код приложения должен быть оформлен в виде maven проекта и размещён на GitHub. Допускается использовать зависимости только из публичных репозиториев. 
Сборка конечного приложения должна быть выполнена командой: 
mvn clean install
Приложение должно быть консольным. 
Пример команды запуска: java -jar orders_parser.jar orders1.csv orders2.json
где orders1.csv и orders2.json файлы для парсинга.
Результат выполнения должен выводиться в stdout поток.
Примечание: в stdout должны попасть только выходные данные, логов там быть не должно.
Парсинг и конвертирование должны выполняться параллельно в несколько потоков.
Необходимо предусмотреть корректную обработку ошибок в исходных файлах. 
Например, вместо числа в файле может быть строковое значение в поле amount.
Разрешается использовать инструменты языка не выше Java 8.
Необходимо учесть возможность добавления новых форматов входящих данных. Например: XLSX
```

### Input data

CSV and JSON files with some test data can be found in `extradata` folder.

### Complexity
Everything is documented, tests will ensure that already written processors 
will work as intended. 

### Project
Spring Framework + few other open-source dependencies list of which 
can be found at the bottom of this document

Console-based app. Accepts as many files, as your stack can handle. 
Will parse each file in its own thread.
Logging is disabled by default. If you'd like to see logs - refer to HowTo section

### Multithreading
Implemented via CompletableFuture
Each file gets its own thread where parsing will be executed. Once all of the
threads finished parsing - program will exit.

### Foolproof
Tests cover invalid data scenarios. On top of that - provided data files
in `extradata` folder already have some corrupted/faulty data to demonstrate
how program handles it

### Expansion time¡
In order to add support for a new file type (for example XML as mentioned in the task)
all you'll need to do is to:
* Add new case in the 
[OrderService](https://github.com/mityavasilyev/interview-unlimint-parser/blob/master/src/main/java/io/github/mityavasilyev/interviewunlimintparser/service/OrderServiceImpl.java)
* Implement [FileToOrderParser Interface](https://github.com/mityavasilyev/interview-unlimint-parser/blob/master/src/main/java/io/github/mityavasilyev/interviewunlimintparser/parser/FileToOrdersParser.java)
in the new parser Class

This way OrderService serves as an entrypoint that only cares about files that 
are passed down to it. Figuring out parsing strategy will not burden potential user


# Used libraries

| Library                                                             | But Why Tho                          |
|---------------------------------------------------------------------|--------------------------------------|
| [Jackson](https://github.com/FasterXML/jackson-databind)            | To handle JSON parsing the smart way |
| [OpenCSV](https://sourceforge.net/p/opencsv/source/ci/master/tree/) | To handle CSV parsing the easy way   |
| [Apache Commons IO](https://github.com/apache/commons-io)           | Helps with IO interactions           |
| [Project Lombok](https://github.com/projectlombok/lombok)           | Saves the day with java8 boilerplate |
