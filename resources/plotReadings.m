readings1 = csvread("./2modules/reading1_1.csv");
readings2 = csvread("./2modules/reading2_1.csv");
readings3 = csvread("./2modules/reading3_1.csv");
readings4 = csvread("./2modules/reading4_1.csv");
x = 0:1:length(readings1(1:6:end))-1;
plot(
%    x, readings1(1:6:end), "-;1linX;",
%    x, readings1(2:6:end), "-;1linY;",
%    x, readings1(3:6:end), "-;1linZ;",
    x, readings1(4:6:end), "-k;1angX;",
    x, readings1(5:6:end), "-k;1angY;",
    x, readings1(6:6:end), "-k;1angZ;",
%    x, readings2(1:6:end), ":;2linX;",
%    x, readings2(2:6:end), ":;2linY;",
%    x, readings2(3:6:end), ":;2linZ;",
    x, readings2(4:6:end), ":r;2angX;",
    x, readings2(5:6:end), ":r;2angY;",
    x, readings2(6:6:end), ":r;2angZ;",
    x, readings3(4:6:end), "-g;1angX;",
    x, readings3(5:6:end), "-g;1angY;",
    x, readings3(6:6:end), "-g;1angZ;",
    x, readings4(4:6:end), ":b;2angX;",
    x, readings4(5:6:end), ":b;2angY;",
    x, readings4(6:6:end), ":b;2angZ;"
)