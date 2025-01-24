^:pow

read mcp and got cobblestone generate frequency:
LAVA update rate:10 ticks in nether and 30 ticks other.(hard-coded)(mark as P)
also receives random tick.


assume random tick is T(default:3 in java and 1 in bedrock)

chance for a block to receive random tick(as A)
```
A = C(1,T)*(1/(16^3))*(((16^3 -1 )/(16^3))^(T-1)) + C(2,T)*((1/(16^3))^2)*(((16^3 -1 )/(16^3))^(T-2)) + ... + C(T,T)*((1/(16^3))^T)*(((16^3 -1 )/(16^3))^(0))
or
A = 1-C(0,T)*(((16^3 -1 )/(16^3))^(T))
```
16^3 is size of a sub-chunk.C(n,k) is ["combination number"](https://en.wikipedia.org/wiki/Combination)(n choose k)IT'S [BINOMIAL DISTRIBUTION](https://en.wikipedia.org/wiki/Binomial_distribution).

so first tick we have chance A to generate CobbleStone(chance 1-A otherwise)
the second tick is (1-A) * A (assume first tick does not generate),
third:A * (1-A)^2...like [geometric distribution](https://en.wikipedia.org/wiki/Geometric_distribution)
```
$(1-A)^{t-1}A$
t:tick,from 1 to P-1(at tick P it will surely generate cobblestone)
```
chance to not generate by random tick(when all ticks to receive random tick fails):
```math
$(1-A)^{P-1}$
```
expectation:
```
\sum (1-A)^{t-1}A
```