clc; close all;

% 	---IMPORTANT NOTES---
% to run this application, please run this M-file in matlab, I use verion 2018, and follow the command line,
% the output will also be provided in the command line.
% please insure all images are located in the same directory as the M-file
% This file is used to test the images provided

% 	---Assumptions---
% height is 7 m off the ground
% optical axis is 30 degree off the horizon or pointing toward the traffic at 60 degrees respective to the normal to the traffic
% resolution is 640x480
% each pixel is 0.042 degrees
% delta t is 0.1s
% each picture has only one car in it

% The angle at the central pixel i.e (320,240) is 60 degrees
% The angle of the car will increase of decrease relative to this central
% point.

%% Given Data

angle = 60;
Cam_h = 7;
degPerPx = 0.042;
delta_t = 0.1;
speedLimit = 30;
widthLimit = 2.5;

%% Load images

im1 = input('Enter the name of the first image (along with the extension): ','s');
I1 = imread(im1);
im2 = input('Enter the name of the second image (along with the extension): ','s');
I2 = imread(im2);

%% Detect whether the first vehicle is red or blue

c1Firetruck = false; %Initiall it is assumed that none of the cars is a firetruck
c2Firetruck = false;

subplot(1,2,1);
imshow(I1);
subplot(1,2,2);
imshow(I2);

red = 0; %These variables are used to store the reddest and bluest pixel values to determine the color of the vehicle
blu = 0;

[height, width, channels] = size(I1);

r = 255; %This is used to check the bluest pixel which is initially assumed to have a very low values
g = 255;
b = 0;


for h = 1:height
    for w = 1:width
        if I1(h, w, 1)<r && I1(h, w, 2)<g && I1(h, w, 3)>b %The bluest pixel will have a small value of red and green and a large value of blue
            r = I1(h, w, 1); %The rgb values are updated to the current bluest pixel
            g = I1(h, w, 2);
            b = I1(h, w, 3);
            bw = w; %Save the x and y coordinates of the current bluest pixel
            bh = h;
            blu  = b; %Store the value of the bluest color
        end
    end
end

r = 0; %This is used to check the reddest pixel which is initially assumed to have a very low values
g = 255;
b = 255;
red = 0;

for h = 1:height
    for w = 1:width
        if I1(h, w, 1)>r && I1(h, w, 2)<g && I1(h, w, 3)<b %The reddest pixel will have a small value of red and green and a large value of blue
            r = I1(h, w, 1); %The rgb values are updated to the current reddest pixel
            g = I1(h, w, 2);
            b = I1(h, w, 3);
            bw = w; %Save the x and y coordinates of the current reddest pixel
            bh = h;
            red  = r; %Store the value of the reddest color
        end
    end
end

if blu>red %Check if the bluest color found in the image is greater than the reddest color.
    c1Firetruck = false; %If the bluest pixel has a higher values than the reddest one then the car is not a firetruck
    
    r = 255; %If the car blue find out the blues pixel in the image
    g = 255; 
    b = 0;

    [height, width, channels] = size(I1);
    for h = 1:height
        for w = 1:width
            if I1(h, w, 1)<r && I1(h, w, 2)<g && I1(h, w, 3)>b
                r = I1(h, w, 1);
                g = I1(h, w, 2);
                b = I1(h, w, 3);
                bw = w; %Store the bluest pixel's location
                bh = h;
            end
        end
    end
    
    
    %Store all the color channels in individual arrays
    r = I1(:, :, 1);             % red
    g = I1(:, :, 2);             % green
    b = I1(:, :, 3);             % blue

    oneColor = double(b) - max(double(r), double(g)); %Only keep the blue color in the image and remove red and green components
    mask = oneColor < 15; %Binarize the image with 15 as a threshold
    lab = bwlabel(mask); %The mask will have groups of 1s, this will label the groups according to their viscinity
    id = lab(bh, bw); %This saves the label of the bluest pixel
    img1 = (lab == id); %We only keep the pixels that have the same values as the label save in the variable 'id'
else %If the value of the reddest pixel is greater than the bluest pixel in the image
    c1Firetruck = true; %The vehicle is a firetruck if the redest color has a a higher value than the blue one.
    %The code remains the same from here onwards as the one seen before,
    %however this time the color that is kept is red and the colors that
    %are being subtracted are blue and red.
    r = 0;
    g = 255;
    b = 255;

    for h = 1:height
        for w = 1:width
            if I1(h, w, 1)>r && I1(h, w, 2)<g && I1(h, w, 3)<b
                r = I1(h, w, 1);
                g = I1(h, w, 2);
                b = I1(h, w, 3);
                bw = w;
                bh = h;
            end
        end
    end

    r = I1(:,:,1);
    g = I1(:,:,2);
    b = I1(:,:,3);

    oneColor = double(r) - max(double(b), double(g));
    mask = oneColor < 20;
    lab = bwlabel(mask);
    id = lab(bh, bw);
    img1 = (lab == id);
end

% figure; imshow(img);

%% Find the properties of the image using regionprops(), this will give us the bounding box, centroid and area of the car.

stats = regionprops('table',img1,'all'); %Save all the propertes of the biarized image in the form of table
centers = stats.Centroid; %Get the centroid information of all the blobs in the image
filledArea = stats.FilledArea; %Get the area of the blobs
filledImage = stats.FilledImage; %Get the images of the blobs
boundingBox = stats.BoundingBox; %Get the coordinates of the bounding boxes around the blobs

%Since there are a lot of blobs in the image we only need to save the data
%of the biggest blob. For that we initialize a few arrays that will store
%only the relevant data.
C1 = [0,0];
D = [0];
FA = [0];
FI = [0];
BB1 = [0,0,0,0];
for i = 1:length(centers) %Iterate through all the blobs
    if filledArea(i)>20000 %Only save the data for blobs that have the area greater than 20000 pixels
        C1 = [C1;centers(i,:)];
        D = [D; 10];
        FA = [FA, filledArea(i)];
        FI = [FI, filledImage(i)];
        BB1 = [BB1; boundingBox(i,:)];
    end
end

% figure; imshow(I2);

%% Check whether the second vehicle is red or blue
%The code repeats the same way as seen for the first vehicle.
red = 0;
blu = 0;

[height, width, channels] = size(I2);

r = 255;
g = 255;
b = 0;

for h = 1:height
    for w = 1:width
        if I2(h, w, 1)<r && I2(h, w, 2)<g && I2(h, w, 3)>b
            r = I2(h, w, 1);
            g = I2(h, w, 2);
            b = I2(h, w, 3);
            bw = w;
            bh = h;
            blu  = b;
        end
    end
end

r = 0;
g = 255;
b = 255;
red = 0;

for h = 1:height
    for w = 1:width
        if I2(h, w, 1)>r && I2(h, w, 2)<g && I2(h, w, 3)<b
            r = I2(h, w, 1);
            g = I2(h, w, 2);
            b = I2(h, w, 3);
            bw = w;
            bh = h;
            red  = r;
        end
    end
end

if blu>red
    c2Firetruck = false;
    r = 255;
    g = 255;
    b = 0;

    [height, width, channels] = size(I2);
    for h = 1:height
        for w = 1:width
            if I2(h, w, 1)<r && I2(h, w, 2)<g && I2(h, w, 3)>b
                r = I2(h, w, 1);
                g = I2(h, w, 2);
                b = I2(h, w, 3);
                bw = w;
                bh = h;
            end
        end
    end

    r = I2(:, :, 1);             % red
    g = I2(:, :, 2);             % green
    b = I2(:, :, 3);             % blue

    oneColor = double(b) - max(double(r), double(g));
    mask = oneColor < 15;
    lab = bwlabel(mask);
    id = lab(bh, bw);
    img2 = (lab == id);
else
    c2Firetruck = true;
    r = 0;
    g = 255;
    b = 255;

    for h = 1:height
        for w = 1:width
            if I2(h, w, 1)>r && I2(h, w, 2)<g && I2(h, w, 3)<b
                r = I2(h, w, 1);
                g = I2(h, w, 2);
                b = I2(h, w, 3);
                bw = w;
                bh = h;
            end
        end
    end

    r = I2(:,:,1);
    g = I2(:,:,2);
    b = I2(:,:,3);

    oneColor = double(r) - max(double(b), double(g));
    mask = oneColor < 20;
    lab = bwlabel(mask);
    id = lab(bh, bw);
    img2 = (lab == id);
end

%% Throw error if the two vehicles are not the same

if c1Firetruck ~= c2Firetruck %This will only trigger if the two vehicles are not the same
        error("Error: The two cars are not the same, make sure that both of the pictures are of the same car.");
end

% figure; imshow(img2);

%% Find the properties of the image using regionprops(), this will give us the bounding box, centroid and area of the car.
stats = regionprops('table',img2,'all');
centers = stats.Centroid;
filledArea = stats.FilledArea;
filledImage = stats.FilledImage;
boundingBox = stats.BoundingBox;

C2 = [0,0];
D = [0];
FA = [0];
FI = [0];
BB2 = [0,0,0,0];
for i = 1:length(centers)
    if filledArea(i)>5000
        C2 = [C2;centers(i,:)];
        D = [D; 10];
        FA = [FA, filledArea(i)];
        FI = [FI, filledImage(i)];
        BB2 = [BB2; boundingBox(i,:)];
    end
end

%% Display the binarized images

figure
subplot(1,2,1);
imshow(img1);
hold on
viscircles(C1,D); %Plot a circle at the centroid of blob
rectangle('Position', [BB1(2,1),BB1(2,2),BB1(2,3),BB1(2,4)],'EdgeColor','r','LineWidth',2 ) %Enclose the blob inside a bounding box

subplot(1,2,2);
imshow(img2);
hold on
viscircles(C2,D);
rectangle('Position', [BB2(2,1),BB2(2,2),BB2(2,3),BB2(2,4)],'EdgeColor','r','LineWidth',2 )

%% Calculate the speed of the vehicle

velocity = (C1(2, 2) - C2(2, 2))/delta_t; %Speed in pixels per second

%Camera
% |\
% |___\
% | 60    \
% |           \
% |               \
% |                   \
% |                       \
% |                           \
% |7 meters                       \
% |                                   \
% |                                       \
% |                                           \
% |                                               \
% |                                                   \
% |                                                       \
% |___________________________________________________________\

%                Opposite
% tan(theta) = -----------
%                Adjacent

% Opposite = Adjacent x tan(theta)

dpp1 = degPerPx*(240-C1(2, 2));% This calculates the deviation of the var from the central pixel
dpp2 = degPerPx*(240-C2(2, 2));

dist1 = Cam_h*tan(deg2rad(angle+dpp1));% The distance of the car in the first picture is calculated
dist2 = Cam_h*tan(deg2rad(angle+dpp2));% The distance of the car in the second picture is calculated

delta_dist = (dist2-dist1);% The difference in the distance


velocity = (delta_dist)/(delta_t);% The speed of the car in meters per second
velocity = velocity*2.23694; % The speed is converted into miles per hour

fprintf("Car speed is: %f%s\n", velocity, " miles/hour");

%% Calculate vehicle width

carAng = (C1(2,1) - BB1(2,1))*degPerPx; % The car width in pixels is calculated
distToCar = sqrt(dist1^2 + Cam_h^2); % The distance between the camera and the car calculated
carWidth = tan(deg2rad(carAng))*distToCar*2;% Finally the width of the car is calculated

fprintf("Car width is: %f%s\n", carWidth, " meters");

%% If the car is over speeding or its width is greater than the allowed limit give a warning

if carWidth>widthLimit
    fprintf("Car width exceeds the limit. Car Width: %f%s\n", carWidth, " m")
    if c1Firetruck == 1
        fprintf("Do not stop the vehicle its a Firetruck!!\n")
    else
        fprintf("The vehicle is not a Firetruck, stop the vehicle!!\n")
    end
end

if velocity>speedLimit
    fprintf("Car speed exceeds the limit. Car Speed: %f%s\n", velocity, " miles/h")
    if c1Firetruck == 1
        fprintf("Do not stop the vehicle its a Firetruck!!\n")
    else
        fprintf("The vehicle is not a Firetruck, stop the vehicle!!\n")
    end
end