N=2000000;%experiment times
count=0;%count that match sum equals 24
sumProdr=0;%total sum of product
prodr=zeros(1,N);
for i=1:N
  r=randi([1 6],1,8);
  sumr=sum(r);
  if sumr==24
    count=count+1;
    sumProdr=sumProdr+prod(r);
  end
end
expProd=sumProdr/count