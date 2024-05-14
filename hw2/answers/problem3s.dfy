method differences(arr:seq<int>) returns (diffs:seq<int>) //for sequences
  requires |arr| > 0
  ensures |diffs| == |arr| - 1
  ensures forall k :: 0 <= k < |diffs| ==> diffs[k] == arr[k + 1] - arr[k]
  {
    diffs := [];
    var a:= 0;
    while a < (|arr| - 1)
      invariant 0 <= a <= |arr| - 1
      invariant |diffs| == a
      invariant forall k :: 0 <= k < |diffs| ==> diffs[k] == arr[k + 1] - arr[k]
      decreases |arr| - 1 - a
    {
        diffs := diffs + [arr[a + 1] - arr[a]];
        a := a + 1;
    }
  }

/*method Main () { //main for sequences
  var input := [4, 5, 8, 2, 8, 9];
  var output := differences(input);
  var i := 0;
  print "Output:   ";
  while i < |output|
  {
    print output[i], " ";
    i := i + 1;
  }
  print "\nExpected: ";
  i := 1;
  while i < |input|
  {
    print input[i]-input[i-1], " ";
    i := i + 1;
  }
}*/