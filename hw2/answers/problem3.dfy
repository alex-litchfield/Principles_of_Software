method differences(arr:array?<int>) returns (diffs:array<int>)//for arrays
  requires arr != null && arr.Length > 0
  ensures diffs.Length == arr.Length - 1
  ensures forall k :: 0 <= k < diffs.Length ==> diffs[k] == arr[k+1] - arr[k]
  {
    diffs := new int[arr.Length - 1];
    var a:= 0;
    while a < diffs.Length 
      invariant 0 <= a <= diffs.Length
      invariant forall k :: 0 <= k < a ==> diffs[k] == arr[k + 1] - arr[k]
      decreases diffs.Length - a
    {
        diffs[a] := arr[a+1]-arr[a];
        a := a + 1;
    }
    
  }

/*method Main() { //main for arrays
  var input := new int[5];
  input[0], input[1], input[2], input[3], input[4] := 4, 5, 8, 2, 8;
  var output := differences(input);
  var i := 0;
  print "Output:   ";
  while i < output.Length
  {
    print output[i], " ";
    i := i + 1;
  }
  print "\nExpected: ";
  i:=1;
  while i < input.Length
  {
    print input[i]-input[i-1], " ";
    i := i + 1;
  }
}*/
