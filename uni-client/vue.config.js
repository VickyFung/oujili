module.exports = {
    devServer: {
		    proxy: {
		      '/wxmapi': {
		        //target: 'http://localhost:8088',
				target: 'http://apitest.yuelaobaobao.com',
				changeOrigin: true,
		        pathRewrite: {
		          '^/wxmapi': ''
		        }
		      }
		    },
	}
}