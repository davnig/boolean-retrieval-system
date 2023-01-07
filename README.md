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

## Development roadmap

- [x] Read corpus from a file
- [x] Normalization
- [x] Indexing
- [x] Save the index on a file
- [x] Load the index from a file
- [x] `AND` queries support
- [ ] `OR` queries support
- [ ] Wildcard queries support
- [ ] Phrase queries support