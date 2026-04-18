module.exports = {
    devServer: {
		    proxy: {
		      '/wxmapi': {
		        //target: 'http://localhost:8088',
				target: 'https://apitest.yuelaobaobao.com',
				changeOrigin: true,
		        pathRewrite: {
		          '^/wxmapi': '/test'
		        }
		      }
		    },
	}
}