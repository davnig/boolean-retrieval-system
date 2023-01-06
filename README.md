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

## Getting started

At startup, the application needs to index the corpus in order to become ready to answer queries. This could take a
while. Instead, you can choose to do a quick start-up, thus loading the already-prepared and encoded index provided
at `data/index.txt`.

### Quick start-up ⚡️

1. Just build and run the application.

The application will become ready to accept queries in 30s, more or less. Just the time to do 10 push-ups. 🏋️‍♂️

### Full start-up ⌛️

Follow these steps if you want to force the application to re-load and index the corpus.

1. Download the corpus [here](https://www.cs.cmu.edu/~ark/personas/);
2. Extract the `MovieSummaries` folder and place it in the project directory;
3. Clear the content of the `data/` directory;
4. Build and run the application.

The application will become ready to accept queries in 15min, more or less. Someone said coffee time? ☕️

## Corpus

The [CMU Movie Summary Corpus](https://www.cs.cmu.edu/~ark/personas/) consists of 42,306 movie plot summaries extracted
from Wikipedia and aligned metadata extracted from Freebase.

## Development roadmap

- [x] Read corpus from a file
- [x] Normalization
- [x] Indexing
- [x] Save the index on a file
- [x] Load the index from a file
- [ ] Standard queries support
- [ ] Wildcard queries support
- [ ] Phrase queries support