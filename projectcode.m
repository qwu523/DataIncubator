%--------------------------------------------------------------------------
%Function1 
%my_min_norm_pdf, this function is applied to #calculate the probability density function fv.
function f=my_min_norm_pdf(x,mu1,sigma1,mu2,sigma2)
f=(1-normcdf(x,mu1,sigma1)).*normpdf(x,mu2,sigma2)+(1-normcdf(x,mu2,sigma2)).*normpdf(x,mu1,sigma1);
%--------------------------------------------------------------------------
%Function2
%my_fun_min_norm,this function is applied to maximize #likelihood function.
function f=my_fun_min_norm(para,v)
alpha=para(1);
beta=para(2);
gamma=para(3);
ruo=para(4);
mu=para(5);
delta_pri=para(6);
delta_pub=para(7);
sigma=para(8);
alpha=normcdf(alpha);
beta=normcdf(beta);
gammma=normcdf(gamma);
ruo=normcdf(ruo);
delta_pri=abs(delta_pri);
delta_pub=abs(delta_pub);
f1=alpha*beta*gamma*ruo*my_min_norm_pdf(v,mu+delta_pri+delta_pub,sigma,mu-delta_pub,sigma);
f2=alpha*beta*gamma*(1-ruo)*my_min_norm_pdf(v,mu+delta_pri-delta_pub,sigma,mu+delta_pub,sigma);
f3=alpha*beta*(1-gamma)*my_min_norm_pdf(v,mu+delta_pri,sigma,mu,sigma);
f4=alpha*(1-beta)*gamma*ruo*my_min_norm_pdf(v,mu+delta_pub,sigma,mu-delta_pub+delta_pri,sigma);
f5=alpha*(1-beta)*gamma*(1-ruo)*my_min_norm_pdf(v,mu-delta_pub,sigma,mu+delta_pub+delta_pri,sigma);
f6=alpha*(1-beta)*(1-gamma)*my_min_norm_pdf(v,mu,sigma,mu+delta_pri,sigma);
f7=(1-alpha)*gamma*my_min_norm_pdf(v,mu+delta_pub,sigma,mu-delta_pub,sigma);
f8=(1-alpha)*(1-gamma)*my_min_norm_pdf(v,mu,sigma,mu,sigma);
f=f1+f2+f3+f4+f5+f6+f7+f8;
y=max(f,eps);
f=-sum(log(y));
%--------------------------------------------------------------------------
%Function3
%My_relative_frequency, this function is applied to calculate relative frequency.
function f=my_relative_frequency(x,n)
a=linspace(min(x),max(x),n);
f=hist(x,a)/length(x);
h=figure;
set(h,'color','green')
bar(a,f)
title('Relative frequency')
%--------------------------------------------------------------------------
%Function4
%My_vol_prob(para), this function is applied to estimate I0,I1,I2.
function [I0,I1,I2]=my_vol_prob(para)
alpha=para(1);
beta=para(2);
gamma=para(3);
ruo=para(4);
mu=para(5);
delta_pri=para(6);
delta_pub=para(7);
sigma=para(8);
E(1)=my_mean_min_norm(mu+delta_pri+delta_pub,mu-delta_pub,sigma,sigma);
E(2)=my_mean_min_norm(mu+delta_pri-delta_pub,mu+delta_pub,sigma,sigma);
E(3)=my_mean_min_norm(mu+delta_pri,mu,sigma,sigma);
E(4)=my_mean_min_norm(mu+delta_pub,mu-delta_pub+delta_pri,sigma,sigma);
E(5)=my_mean_min_norm(mu-delta_pub,mu+delta_pub+delta_pri,sigma,sigma);
E(6)=my_mean_min_norm(mu,mu+delta_pri,sigma,sigma);
E(7)=my_mean_min_norm(mu+delta_pub,mu-delta_pub,sigma,sigma);
E(8)=my_mean_min_norm(mu-delta_pub,mu+delta_pub,sigma,sigma);
E(9)=my_mean_min_norm(mu,mu,sigma,sigma);
EV(1)=alpha*beta*gamma*ruo*E(1);
EV(2)=alpha*beta*gamma*(1-ruo)*E(2);
EV(3)=alpha*beta*(1-gamma)*E(3);
EV(4)=alpha*(1-beta)*gamma*ruo*E(4);
EV(5)=alpha*(1-beta)*gamma*(1-ruo)*E(5);
EV(6)=alpha*(1-beta)*(1-gamma)*E(6);
EV(7)=(1-alpha)*gamma*ruo*E(7);
EV(8)=(1-alpha)*gamma*(1-ruo)*E(8);
EV(9)=(1-alpha)*(1-gamma)*E(9);
I0=1-sum(EV(7:9))/sum(EV);
I1=sum(EV(1:3))/sum(EV);
I2=sum(EV(4:6))/sum(EV);




