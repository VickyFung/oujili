module.exports = {
    devServer: {
		    proxy: {
		      '/api/app': {
		        //target: 'http://localhost:8088',
				target: 'http://apitest.yuelaobaobao.com/api/app',
				changeOrigin: true
		      }
		    },
	}
}