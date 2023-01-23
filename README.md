# Boolean Retrieval System

A simple Java implementation of a Boolean Retrieval System. This is the Project #1 of the Information Retrieval
university course.

## Project requirements

Write an IR system able to answer:

- Boolean queries with `AND`, `OR` and `NOT`;
- Wildcard and phrase queries;
- Provide a way to save and load the entire index from disk, to avoid re-indexing when the program starts;
- Some normalization or stemming can be performed;
- Spelling correction can be implemented;
- Evaluate the system on a set of test queries.

## Corpus

The [CMU Movie Summary Corpus](https://www.cs.cmu.edu/~ark/personas/) consists of 42,306 movie plot summaries extracted
from Wikipedia and aligned metadata extracted from Freebase.

## Getting started

In order to answer queries, the system needs to index the entire corpus first. This operation can take a long time so a
pre-built index, located inside the `data` folder, is used by default for faster start-up. To start the system, just
build and run the application. If you want the complete indexing operation to be performed instead, you need to:

1. Download the movie corpus [here](https://www.cs.cmu.edu/~ark/personas/);
2. Extract the `MovieSummaries` folder and place it in the project root folder;
3. Delete the pre-built index or move it in another place;
4. Build and run the application.

## Query support

As required, the system supports boolean queries, single wildcard queries and two-terms phrase queries.

### Boolean queries

Both single- and multiple-term boolean queries are supported. The system expects the boolean operators to be specified
in uppercase. By default, if no operators are specified, the query is converted to an `AND` query. In a `NOT` query,
when multiple terms are specified, the system computes the negation of the OR-ed list of terms.

#### Example of `AND` queries

```text
indiana AND jones
rocky balboa adriana
yoda AND luke AND darth
yoda luke darth
```

#### Example of `OR` queries

```text
yoda OR skywalker
yoda OR luke OR darth OR skywalker
```

#### Example of `NOT` queries

```text
NOT the
NOT and is or be are the on at in
```

### Wildcard queries

```text
cat*
catastroph*
c*lly
*atastrophically
```

### Phrase queries

```text
"forrest gump"
"darth maul"
"order 66"
"rocky balboa"
"indiana jones"
```

## To be fixed

- Non-trailing wildcard queries do not work
- Query computation time displaying always `0 ms`
