# BLOOP Interpreter

**Beginner-Level Object-Oriented Program** — a working interpreter for the BLOOP
scripting language, built in pure Java.

---

## What is BLOOP?

BLOOP is a small scripting language that reads like plain English:

```
put 10 into x
put 3 into y
put x + y * 2 into result
print result

if result > 10 then:
    print "big number"

repeat 3 times:
    print "hello"
```

---

## Project Structure

```
bloop-interpreter/
│
├── src/
│   ├── main/java/com/bloop/
│   │   │
│   │   ├── lexer/                   ← Stage 1: Tokenisation
│   │   │   ├── TokenType.java       — enum of all token kinds
│   │   │   ├── Token.java           — immutable token value object
│   │   │   └── Tokenizer.java       — reads source → List<Token>
│   │   │
│   │   ├── ast/                     ← Expression nodes (Abstract Syntax Tree)
│   │   │   ├── Expression.java      — interface: Object evaluate(Environment)
│   │   │   ├── NumberNode.java      — numeric literal, e.g. 42
│   │   │   ├── StringNode.java      — string literal, e.g. "hello"
│   │   │   ├── VariableNode.java    — variable reference, e.g. x
│   │   │   └── BinaryOpNode.java    — binary op, e.g. x + y * 2
│   │   │
│   │   ├── runtime/                 ← Execution environment
│   │   │   └── Environment.java     — variable store (Map<String,Object>)
│   │   │
│   │   ├── instruction/             ← Stage 3: Executable instructions
│   │   │   ├── Instruction.java     — interface: void execute(Environment)
│   │   │   ├── AssignInstruction.java  — put <expr> into <var>
│   │   │   ├── PrintInstruction.java   — print <expr>
│   │   │   ├── IfInstruction.java      — if <cond> then: <body>
│   │   │   ├── ElseInstruction.java    — if <cond> then: <body> else: <body>
│   │   │   └── RepeatInstruction.java  — repeat N times: <body>
│   │   │
│   │   ├── parser/                  ← Stage 2: Token list → Instruction list
│   │   │   └── Parser.java
│   │   │
│   │   ├── interpreter/             ← Pipeline orchestrator
│   │   │   └── Interpreter.java     — run(sourceCode): wires all three stages
│   │   │
│   │   └── main/                    ← CLI entry point
│   │       └── Main.java            — reads .bloop file, calls Interpreter
│   │
│   └── test/resources/programs/     ← Sample BLOOP programs
│       ├── program1_arithmetic.bloop
│       ├── program2_strings.bloop
│       ├── program3_conditional.bloop
│       ├── program4_loop.bloop
│       ├── program5_combined.bloop
│       └── program6_else_extension.bloop
│
├── out/                             ← Compiled .class files (generated)
├── build.sh                         ← Compile + test script
└── README.md
```

---

## How to Build and Run

### 1. Compile

```bash
# From the project root:
find src/main/java -name "*.java" | xargs javac -d out
```

Or use the build script:

```bash
chmod +x build.sh
./build.sh
```

### 2. Run a .bloop file

```bash
java -cp out com.bloop.main.Main path/to/program.bloop
```

### 3. Run all sample programs

```bash
./build.sh
```

---

## The Interpreter Pipeline

```
  .bloop source file
        │
        ▼
  ┌─────────────┐
  │  Tokenizer  │  reads char-by-char → List<Token>
  └─────────────┘
        │
        ▼
  ┌─────────────┐
  │   Parser    │  reads token list → List<Instruction>
  └─────────────┘
        │
        ▼
  ┌─────────────────────────────┐
  │  Execute each Instruction   │  against a shared Environment
  └─────────────────────────────┘
        │
        ▼
     stdout output
```

---

## BLOOP Language Reference

### Assignment
```
put <expression> into <variableName>
```
```
put 10 into x
put x + y * 2 into result
put "hello" into greeting
```

### Print
```
print <expression>
```
```
print result
print "Hello from BLOOP"
```

### Conditional
```
if <condition> then:
    <body>
```
```
if score > 50 then:
    print "Pass"
```

### Conditional with Else (Extension)
```
if <condition> then:
    <then-body>
else:
    <else-body>
```
```
if score > 50 then:
    print "Pass"
else:
    print "Fail"
```

### Loop
```
repeat <count> times:
    <body>
```
```
repeat 4 times:
    print i
    put i + 1 into i
```

### Arithmetic Operators
| Operator | Meaning        |
|----------|----------------|
| `+`      | Addition       |
| `-`      | Subtraction    |
| `*`      | Multiplication |
| `/`      | Division       |

### Comparison Operators
| Operator | Meaning           |
|----------|-------------------|
| `>`      | Greater than      |
| `<`      | Less than         |
| `==`     | Equal to          |

### Operator Precedence
`*` and `/` bind more tightly than `+` and `-`.  
Comparisons (`> < ==`) are evaluated last.

```
x + y * 2    →  x + (y * 2)     ✓
score > 50   →  (score) > (50)  ✓
```

### Comments
Lines starting with `#` are ignored:
```
# This is a comment
put 10 into x   # inline comment not supported — use full-line only
```

---

## Sample Programs and Expected Output

### Program 1 — Arithmetic
```
put 10 into x
put 3 into y
put x + y * 2 into result
print result
```
**Output:** `16`

### Program 2 — Strings
```
put "Sitare" into name
print name
print "Hello from BLOOP"
```
**Output:**
```
Sitare
Hello from BLOOP
```

### Program 3 — Conditional
```
put 85 into score
if score > 50 then:
    print "Pass"
```
**Output:** `Pass`

### Program 4 — Loop
```
put 1 into i
repeat 4 times:
    print i
    put i + 1 into i
```
**Output:**
```
1
2
3
4
```

---

## Extension Implemented: Else Block

The interpreter supports an optional `else:` branch on any `if` statement.

```
put 30 into score
if score > 50 then:
    print "Pass"
else:
    print "Fail"
```
**Output:** `Fail`

When the condition is `true`, only the `then:` body runs.  
When the condition is `false`, only the `else:` body runs.

---


