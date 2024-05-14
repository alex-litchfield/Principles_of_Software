method sumn(n: int) returns (t: int) 
  requires n >= 0
  ensures t == n * (n + 1) / 2
{
  var i:= 0;
  t := 0;
  while (i < n) 
    invariant 0 <= i <= n
    invariant t == i * (i + 1) / 2
    decreases n - i
  {
    i := i + 1;
    t := t + i;
  }
}


/*method Main() {
  var tests := [[0, 0], [1, 1], [2, 3], [6, 21]];

  var c:int;
  var i := 0;
  while i < |tests|
    invariant i <= |tests|
  {
    c := sumn(tests[i][0]);
    print "sumn(", tests[i][0], ") = ", c, ", expected ", tests[i][1], "\n\n";
    i := i + 1;
  }
}*/