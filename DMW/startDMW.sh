######This Script sets up our server######
#Author: Saleh Nawar
#UP918156

##UPDATE VM & INSTALL DEPENDICIES
echo "Updating.."
sudo apt-get update
echo "Update Completed.."
echo "Installing stuff.."
curl -sL https://deb.nodesource.com/setup_12.x |sudo -E bash - 
sudo apt-get install -y git nodejs
echo "*********Installed dependinces..*************"


#fetch key & server from metadata
key=`openssl rand -base64 32`
server=`(curl "http://metadata.google.internal/computeMetadata/v1/instance/network-interfaces/0/access-configs/0/external-ip" -H "Metadata-Flavor: Google")`


echo "*********Installing DMW software..************ "
#clone & setup master worker software
git clone https://github.com/portsoc/distributed-master-worker
cd distributed-master-worker
npm install
echo "******DMW Installed********"
a=$1
if [ -z "$a" ] #if NULL
then
	echo "Please enter number of workers"
	echo "Syntax is : ./startDMW.sh NUM"
	exit
elif [ $a -gt 5 ] || [ $a -le 0 ] #if a > 5 or <= 0
then
	echo "Please choose a number of workers between 1 and 5 "
	exit
else  
while [ $a -gt 0 ] #iterrate to create workers
do
	
	cd ~
	
	echo "************Making Worker-"$a
	gcloud compute instances create \
 	 --zone "europe-west1-b" \
 	 --machine-type f1-micro \
 	 --metadata key=$key,server=$server \
	 --metadata-from-file startup-script=startClient.sh \
	  worker-$a &
	a=`expr $a - 1`
	cd distributed-master-worker

done
fi

#run server
sudo npm run server $key  

wait # wait for workers to instantiate 
echo "**********Work Done***********"

#Delete Workers
a=$1
# change this if you have delete in a diffrent directory
cd ~
while [ $a -gt 0 ]
do
	./delete worker-$a &
	echo "deleting worker- "$a
	a=`expr $a - 1`
done
wait
echo "************Workers Deleted************"
