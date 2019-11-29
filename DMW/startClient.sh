#Author: Saleh Nawar
#UP918156
##UPDATE VM & INSTALL DEPENDICIES
#update VM & install dependinces
echo "Updating.."
sudo apt-get update
echo "Update Completed.."
echo "Installing stuff.."
curl -sL https://deb.nodesource.com/setup_12.x |sudo -E bash -
sudo apt-get install -y git nodejs
echo "************Installed dependinces for worker..***************"
#clone & setup master worker software
git clone https://github.com/portsoc/distributed-master-worker
cd distributed-master-worker
npm install
echo "************Installed DMW*************"
#fetch key & server IP from metadata
key=`(curl "http://metadata.google.internal/computeMetadata/v1/instance/attributes/key" -H "Metadata-Flavor: Google")`
server=`(curl "http://metadata.google.internal/computeMetadata/v1/instance/attributes/server" -H "Metadata-Flavor: Googl
e")`
#run client
npm run client $key $server
echo "***********Workers Done***********"