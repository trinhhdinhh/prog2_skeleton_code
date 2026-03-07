# Geaux Parser - Abstract Syntax Tree Builder

A parser and Abstract Syntax Tree (AST) builder for the Geaux programming language, implemented using ANTLR 4 in Java.

## Overview

This project implements a complete parser for Geaux, a C-like programming language. The parser takes Geaux source code as input and produces an Abstract Syntax Tree (AST) representation of the program structure. This AST serves as an intermediate representation that can be used for further compiler phases such as semantic analysis, optimization, and code generation.

## What is Geaux?

Geaux is a statically-typed programming language with syntax similar to C/Go, featuring:

- **Type System**: Support for primitive types (int, string), arrays, pointers, structs, unions, and typedefs
- **Functions**: First-class function declarations with parameters and return types
- **Control Flow**: If/else statements, while loops, break statements, and return statements
- **Expressions**: Binary operations, unary operations, function calls, array indexing, and assignments
- **Declarations**: Variable declarations with initializers, function declarations, struct/union definitions

### Example Geaux Code

```geaux
fun int main() {
   var int[][] x = {{1,2,3},{1,2,3}};
   return 0;
}
```

## Project Structure

```
prog2_skeleton_code/
├── Absyn/              # Abstract Syntax Tree node definitions
│   ├── Absyn.java      # Base class for all AST nodes
│   ├── Decl.java       # Declaration nodes
│   ├── Exp.java        # Expression nodes
│   ├── Stmt.java       # Statement nodes
│   └── ...             # Specific node implementations
├── Parse/              # Parser implementation
│   ├── gParser.g4      # ANTLR grammar definition for Geaux
│   ├── ASTBuilder.java # Visitor that builds AST from parse tree
│   ├── ParserDriver.java # Main driver program
│   └── antlr_build/    # Generated ANTLR parser files
├── examples/           # Example Geaux programs
│   ├── Geaux1.g
│   ├── Geaux2.g
│   └── ...
├── tests/              # Test cases
├── lib/                # External libraries (ANTLR)
├── bin/                # Compiled class files
├── run.sh              # Build and run script
└── Makefile            # Build configuration
```

## How It Works

The parser operates in three main stages:

### 1. Lexical Analysis (Tokenization)
The ANTLR-generated lexer (`gLexer`) reads the source file and converts it into a stream of tokens (keywords, identifiers, operators, literals, etc.).

### 2. Syntax Analysis (Parsing)
The ANTLR-generated parser (`gParser`) consumes the token stream and builds a parse tree according to the grammar rules defined in `gParser.g4`. This validates that the program follows Geaux's syntax rules.

### 3. AST Construction
The `ASTBuilder` class (a visitor) traverses the parse tree and constructs a simplified Abstract Syntax Tree using the node classes from the `Absyn/` package. This AST removes syntactic details and represents the program's semantic structure.

## Building and Running

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- ANTLR 4.13.2 (included in `lib/`)
- Unix-like environment with bash

### Quick Start

Run a Geaux program using the provided script:

```bash
./run.sh examples/Geaux1.g
```

Or run the default test file:

```bash
./run.sh
```

### Manual Build Process

```bash
# Set up classpath
export CLASSPATH=$(pwd)/bin/:$(pwd)/lib/*

# Clean previous build
make clean

# Generate ANTLR parser
mkdir -p ./Parse/antlr_build/Parse
java -jar ./lib/antlr-4.13.2-complete.jar \
   -visitor Parse/gParser.g4 \
   -o ./Parse/antlr_build

# Compile Java sources
find . -name "*.java" > sources.txt
javac -d bin @sources.txt
rm sources.txt

# Run parser
java Parse.ParserDriver <input_file.g>
```

### Cleaning Build Artifacts

```bash
make clean
```

This removes the `bin/` directory and generated ANTLR files.

## Implementation Details

### Abstract Syntax Tree Nodes

The `Absyn/` package contains classes representing different AST node types:

- **Declarations**: `VarDecl`, `FunDecl`, `StructDecl`, `UnionDecl`, `Typedef`
- **Statements**: `CompStmt`, `IfStmt`, `WhileStmt`, `ExprStmt`, `ReturnStmt`, `BreakStmt`
- **Expressions**: `BinOp`, `UnaryExp`, `FunExp`, `ArrayExp`, `AssignExp`, `ID`, `DecLit`, `StrLit`
- **Types**: `Type`, `ArrayType`
- **Lists**: `DeclList`, `StmtList`, `ExpList`

### ASTBuilder Visitor

The `ASTBuilder.java` file implements the visitor pattern to convert ANTLR's parse tree into AST nodes. Key visitor methods include:

- `visitProgram()` - Entry point, builds top-level declaration list
- `visitVarDecl()`, `visitFunDecl()` - Handle declarations
- `visitIfStmt()`, `visitWhileStmt()` - Handle control flow
- `visitBinOp()`, `visitFunExp()` - Handle expressions
- `visitType()` - Handle type specifications

### Special Features

- **Multiple Number Systems**: The parser supports decimal, hexadecimal (0x prefix), and octal (0 prefix) literals
- **Array Types**: Supports multi-dimensional arrays with both empty brackets `[]` and sized brackets `[expr]`
- **Nested Initializers**: Handles complex initialization expressions including nested array initializers
- **Type Modifiers**: Supports `const` qualifier and pointer types with `*`

## Testing

The project includes example programs in the `examples/` directory and test cases in the `tests/` directory. To test the parser:

```bash
# Run individual examples
./run.sh examples/Geaux1.g
./run.sh examples/Geaux2.g

# Run test suite (if available)
./test.sh
```

## Output

When run, the parser outputs a textual representation of the AST. For example:

```
Starting Parser
DeclList [
  FunDecl(
    Type: int
    Name: main
    Parameters: []
    Body: CompStmt [...]
  )
]
```

## Development

### Adding New Language Features

1. Update the grammar in `Parse/gParser.g4`
2. Regenerate ANTLR files
3. Create corresponding AST node classes in `Absyn/`
4. Implement visitor methods in `Parse/ASTBuilder.java`

### Project Components

- **Part 1**: Type system and basic literals
- **Part 2**: Declarations (variables, functions, structs, unions, typedefs)
- **Part 3**: Statements and control flow
- **Part 4**: Expressions (binary operations, function calls, array access, assignments)

## License

Educational project - part of a compiler construction course.

## Notes

- This is Project 2 in a compiler construction sequence
- The grammar file (`gParser.g4`) is complete and provided
- The implementation focuses on building the AST visitor pattern
- Line numbers in AST nodes are initialized to 0 for this phase
