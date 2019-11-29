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
server=`(curl "http://metadata.google.internal/computeMetadata/v1/instance/network-interfaces/0/access-configs/0/externa
l-ip" -H "Metadata-Flavor: Google")`
#setup workers
gcloud compute instances create \
 --zone "europe-west1-b" \
 --machine-type f1-micro \
 --metadata key=$key,server=$server \
 --metadata-from-file startup-script=startClient.sh \
  worker
echo "*********Installing DMW software..************ "
#clone & setup master worker software
git clone https://github.com/portsoc/distributed-master-worker
cd distributed-master-worker
npm install
echo "******DMW Installed********"
#run server
sudo npm run server $key
echo "Work Done"