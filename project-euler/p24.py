
#assumes l is sorted
def permute(l):
    if len(l) == 1:
        return [[l[0]]]
    result = []
    for i in range(len(l)):
        #copy to tmp
        tmp = list(l)
        del tmp[i]
        for p in permute(tmp):
            r = [l[i]] + p
            result.append(r)
    return result

print permute([1])
print ()
print ()
print permute([1,2])
print ()
print ()
print permute([1,2,3])
l = permute([0,1,2,3,4,5,6,7,8,9])
l.sort()
print "found", len(l), "permutations"
print "result is", l[999999]
