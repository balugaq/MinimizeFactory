it's not quite helpful because i don't know how to combine cobbblestone-generation with wither break block period

read mcp and got cobblestone generate frequency:
LAVA update rate:10 ticks in nether and 30 ticks other.(hard-coded)(mark as P)
also receives random tick.


assume random tick is T(default:3 in java and 1 in bedrock)

chance for a block to receive random tick(as A)
```math
A = C_T^1 \cdot (\frac{1}{16^3})^{1} \cdot (\frac{16^3-1}{16^3})^{T-1} + C_T^2 \cdot (\frac{1}{16^3})^{2} \cdot (\frac{16^3-1}{16^3})^{T-2} + ... + C_T^T \cdot (\frac{1}{16^3})^{T} \cdot (\frac{16^3-1}{16^3})^{0}
```
or
```math
A = 1-C_T^0 \cdot (\frac{16^3-1}{16^3})^T
```
16^3 is size of a sub-chunk.IT'S [BINOMIAL DISTRIBUTION](https://en.wikipedia.org/wiki/Binomial_distribution).

so first tick we have chance A to generate CobbleStone(chance 1-A otherwise)
the second tick is (1-A) * A (assume first tick does not generate),
third:A * (1-A)^2...like [geometric distribution](https://en.wikipedia.org/wiki/Geometric_distribution)
```math
(1-A)^{t-1}A
```
t:tick,from 1 to P-1(at tick P it will surely generate cobblestone)

expectation for 1-P ticks:
```math
\frac{\sum_{t=1}^{P-1} (1-A)^{t-1}A}{P}
```
maybe it's not easy to see that
```math
\sum_{t=1}^{P-1}(1-A)^{t-1}A = A\frac{1-(1-A)^{P-1}}{1-(1-A)}={1-(1-A)^{P-1}}
```
after this,expectation for 1-P ticks:
```math
\frac{1-(1-A)^{P-1}}{P}
```
so in P ticks we have total expectation:
```math
\frac{\frac{1-(1-A)^{P-1}}{P}\cdot(P-1)+1}{P}=\frac{(1-(1-A)^{P-1})\cdot(P-1)+P}{P^2}=\frac{2P-1-(1-A)^{P-1}\cdot(P-1)}{P^2}
```
These above assume that we break cobblestone costs no ticks.(this cannot happen)

```
wither break block condition:
a wither has a int:blockBreakCounter
once ai update(trigger in every tick),blockBreakCounter -= 1.
wither breaks block once (blockBreakCounter==0) after above
once attacked and (blockBreakCounter<0),set value (blockBreakCounter=20)

use snow golem as attack source.it's attack period is also 20
we can assume that wither is damaged once break block.So that wither break block every 20 ticks.
```
In fact I didn't learn statistics before.I don't know how to calculate next.