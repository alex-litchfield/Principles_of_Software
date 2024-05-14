method loopysqrt(n:int) returns (root:int)
  ensures root * root <= n || n < 0 //root * root is either n (perfect square), or less than
                                    //n (number doesnt have perfect square), or n < 0 so invalid
  {
    var a := n;
    root := 0;
    while (a > 0)
      decreases a
      invariant root * root <= n - a
      invariant 0 <= a <= n || 0 > a >= n
    {
      root := root + 1;
      a := a - (2 * root - 1);
      //if statement is the addition
      if (a < 0) {
        root := root - 1;
        a := 0;
      }
    }
  }

/*method Main() {
  var tests := [[-1, 0], [4, 2], [8, 2], [9, 3], [36, 6], [64, 8]];

  var c:int;
  var i := 0;
  while i < |tests|
    invariant i <= |tests|
  {
    c := loopysqrt(tests[i][0]);
    print "loopysqrt(", tests[i][0], ") = ", c, ", expected ", tests[i][1], "\n\n";
    i := i + 1;
  }
}*/
