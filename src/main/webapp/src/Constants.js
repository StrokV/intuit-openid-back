const prod = {
    url: {
        API_URL: 'http://intuitoidcback-env-1.eba-z35zkppj.us-east-2.elasticbeanstalk.com/'
    }
};

const dev = {
    url: {
        API_URL: 'http://localhost:8080/'
    }
};

export const config = process.env.NODE_ENV === 'development' ? dev : prod;
