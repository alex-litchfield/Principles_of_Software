method dutch(arr: array?<char>) returns (k: int)
  modifies arr
  requires arr != null
  requires forall j :: 0 <= j < arr.Length ==> arr[j] == 'b' || arr[j] == 'r'

  ensures 0 <= k <= arr.Length
  ensures forall l :: 0 <= l < k ==> arr[l] == 'r'
  ensures forall m :: k <= m < arr.Length ==> arr[m] == 'b'
  {
    k := 0;
    var i := 0;
    while (i < arr.Length)
      invariant 0 <= k <= i
      invariant i <= arr.Length
      invariant forall p :: 0 <= p < k ==> arr[p] == 'r'
      invariant forall o :: k <= o < i ==> arr[o] == 'b'
      invariant forall n :: 0 <= n < arr.Length ==> arr[n] == 'r' || arr[n] == 'b'
      decreases arr.Length - i
      {
          if (arr[i] == 'r') {
              arr[i] := 'b'; //set b needs to go first so that r can be reset in some instances
              arr[k] := 'r';
              k := k + 1;
          }
          i := i + 1;
      }
  }



/*method Main() {
    var input := new char[10];
    input[0], input[1], input[2], input[3], input[4], input[5], input[6], input[7], input[8], input[9] := 'r', 'b', 'r', 'b', 'r', 'b', 'r', 'b', 'r', 'b';
    var answer := dutch(input);
    print(answer);
}*/