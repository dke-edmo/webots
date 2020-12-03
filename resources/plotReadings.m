readings1 = csvread("./reading1.csv");
readings2 = csvread("./reading2.csv");
x = 0:1:length(readings1(1:6:end))-1;
plot(
%    x, readings1(1:6:end), "-;1linX;",
%    x, readings1(2:6:end), "-;1linY;",
%    x, readings1(3:6:end), "-;1linZ;",
    x, readings1(4:6:end), "-;1angX;",
    x, readings1(5:6:end), "-;1angY;",
    x, readings1(6:6:end), "-;1angZ;",
%    x, readings2(1:6:end), ":;2linX;",
%    x, readings2(2:6:end), ":;2linY;",
%    x, readings2(3:6:end), ":;2linZ;",
    x, readings2(4:6:end), ":;2angX;",
    x, readings2(5:6:end), ":;2angY;",
    x, readings2(6:6:end), ":;2angZ;"
)