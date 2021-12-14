# LZ77CompressionAlgorithm

Simple LZ77 Compression Encoding program written using Java.

## Encoding Example:

### Input:

![example](https://user-images.githubusercontent.com/73957889/146095102-2424326e-a171-4dd6-ba9f-3579fb930454.png)

#### Charstream for 8x8 image above:

```
AAABBAAAAABCCBAAABCDDDBABCDEEDCBBDEBBEDBABDCCDBAAABDCBAAAAABBAAA
```

Where:
```
White     -> A
Black     -> B
Green     -> C
Grey      -> D
Dark Grey -> E
```

### Output:
```
Encoding Table:
-----------------------------------------------------------------
| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | A | B | C | D | E | F | 
-----------------------------------------------------------------
|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |  A A A B B A A A A A B C C B A A A B C D D D B A B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   | A |  A A B B A A A A A B C C B A A A B C D D D B A B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
|   |   |   |   |   |   |   |   |   |   |   |   |   |   | A | A |  A B B A A A A A B C C B A A A B C D D D B A B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
|   |   |   |   |   |   |   |   |   |   |   |   |   | A | A | A |  B B A A A A A B C C B A A A B C D D D B A B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
|   |   |   |   |   |   |   |   |   |   |   |   | A | A | A | B |  B A A A A A B C C B A A A B C D D D B A B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
|   |   |   |   |   |   |   |   |   |   |   | A | A | A | B | B |  A A A A A B C C B A A A B C D D D B A B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
|   |   |   |   |   |   |   |   | A | A | A | B | B | A | A | A |  A A B C C B A A A B C D D D B A B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
|   |   |   |   |   | A | A | A | B | B | A | A | A | A | A | B |  C C B A A A B C D D D B A B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
|   |   |   |   | A | A | A | B | B | A | A | A | A | A | B | C |  C B A A A B C D D D B A B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
|   |   |   | A | A | A | B | B | A | A | A | A | A | B | C | C |  B A A A B C D D D B A B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| A | A | B | B | A | A | A | A | A | B | C | C | B | A | A | A |  B C D D D B A B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| B | B | A | A | A | A | A | B | C | C | B | A | A | A | B | C |  D D D B A B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| B | A | A | A | A | A | B | C | C | B | A | A | A | B | C | D |  D D B A B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| A | A | A | A | A | B | C | C | B | A | A | A | B | C | D | D |  D B A B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| A | A | A | A | B | C | C | B | A | A | A | B | C | D | D | D |  B A B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| A | A | B | C | C | B | A | A | A | B | C | D | D | D | B | A |  B C D E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| C | C | B | A | A | A | B | C | D | D | D | B | A | B | C | D |  E E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| C | B | A | A | A | B | C | D | D | D | B | A | B | C | D | E |  E D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| B | A | A | A | B | C | D | D | D | B | A | B | C | D | E | E |  D C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| A | A | A | B | C | D | D | D | B | A | B | C | D | E | E | D |  C B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| A | A | B | C | D | D | D | B | A | B | C | D | E | E | D | C |  B B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| A | B | C | D | D | D | B | A | B | C | D | E | E | D | C | B |  B D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| B | C | D | D | D | B | A | B | C | D | E | E | D | C | B | B |  D E B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| D | D | D | B | A | B | C | D | E | E | D | C | B | B | D | E |  B B E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| D | B | A | B | C | D | E | E | D | C | B | B | D | E | B | B |  E D B A B D C C D B A A A B D C B A A A A A B B A A A  
| A | B | C | D | E | E | D | C | B | B | D | E | B | B | E | D |  B A B D C C D B A A A B D C B A A A A A B B A A A  
| B | C | D | E | E | D | C | B | B | D | E | B | B | E | D | B |  A B D C C D B A A A B D C B A A A A A B B A A A  
| C | D | E | E | D | C | B | B | D | E | B | B | E | D | B | A |  B D C C D B A A A B D C B A A A A A B B A A A  
| E | E | D | C | B | B | D | E | B | B | E | D | B | A | B | D |  C C D B A A A B D C B A A A A A B B A A A  
| E | D | C | B | B | D | E | B | B | E | D | B | A | B | D | C |  C D B A A A B D C B A A A A A B B A A A  
| D | C | B | B | D | E | B | B | E | D | B | A | B | D | C | C |  D B A A A B D C B A A A A A B B A A A  
| B | D | E | B | B | E | D | B | A | B | D | C | C | D | B | A |  A A B D C B A A A A A B B A A A  
| D | E | B | B | E | D | B | A | B | D | C | C | D | B | A | A |  A B D C B A A A A A B B A A A  
| E | D | B | A | B | D | C | C | D | B | A | A | A | B | D | C |  B A A A A A B B A A A  
| B | D | C | C | D | B | A | A | A | B | D | C | B | A | A | A |  A A B B A A A  
| C | D | B | A | A | A | B | D | C | B | A | A | A | A | A | B |  B A A A  
| A | A | B | D | C | B | A | A | A | A | A | B | B | A | A | A |   

Codestream: 
A A A B B <B:3> <9:3> C C <7:4> <9:2> D D D <7:2> <9:3> E E D C B B <9:2> <C:2> <7:2> B A <7:2> C C <9:3> A <7:4> <9:4> <7:3> <2:4> 

Efficiency: 
0.4375 ≃ 44% reduction.
```
