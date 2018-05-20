<?xml version="1.0" encoding="UTF-8"?>
<exchange name="OK" date="1481616772605" status="200" duration=82>
	<values>
		<value name="pageNumber" direction="REQUEST" location="QUERY" type="TEXT" value="1" bodyType="RAW"/>
	</values>
</exchange>



<project name="LNS" baseUrl="http://149.202.168.118:45800">
	<path name="application">
		<path name="stations">
			<endpoint name="getStations" path="/application/stations"
				method="GET">
				<exchanges>
					<exchange name="echange" date="1481616772605" status="200">
						<request
							uri="http://149.202.168.118:45800/application/stations?pageSize=200&amp;pageNumber=1">
							<body />
							<parameters>
								<parameter enabled="true" location="HEADER"
									name="Authorization"
									value="Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlcm9zcyIsInJvbGUiOiJTVVBFUl9BRE1JTiIsImdyb3VwSWQiOiI2IiwiaXNzIjoib3NzQ2xpZW50IiwiZXhwIjoxNDgxNjUyNTY4fQ.zcHZz7sNEEqIDJ6UyMAcBkBaZCH8CmWdp0BGAuM7OMQ" />
								<parameter enabled="true" location="QUERY"
									name="pageNumber" value="1" />
								<parameter enabled="true" location="QUERY"
									name="pageSize" value="200" />
							</parameters>
						</request>
						<response status="200">
							<body>{"page":1,"nbPages":1,"pageSize":200,"count":128,"totalCount":128,"dtos":[{"stationId":"00-00-00-00-00-12-40-00","longitude":-1.67429,"latitude":48.11198,"altitude":39},{"stationId":"00-00-00-00-00-12-40-01","longitude":-0.80422,"latitude":48.98205,"altitude":39},{"stationId":"00-00-00-00-00-12-40-02","longitude":-0.14086,"latitude":49.64541,"altitude":39},{"stationId":"00-00-00-00-00-12-40-03","longitude":-0.0192,"latitude":49.76707,"altitude":39},{"stationId":"00-00-00-00-00-12-40-04","longitude":0.72246,"latitude":50.50873,"altitude":39},{"stationId":"00-00-00-00-00-12-40-05","longitude":1.33159,"latitude":51.11786,"altitude":39},{"stationId":"00-00-00-00-00-12-40-06","longitude":1.49235,"latitude":51.27862,"altitude":39},{"stationId":"00-00-00-00-00-12-40-07","longitude":2.19838,"latitude":51.98465,"altitude":39},{"stationId":"00-00-00-00-00-12-40-08","longitude":2.67071,"latitude":52.45698,"altitude":39},{"stationId":"00-00-00-00-00-12-40-09","longitude":2.983,"latitude":52.76927,"altitude":39},{"stationId":"00-00-00-00-00-12-40-0A","longitude":3.34916,"latitude":53.13543,"altitude":39},{"stationId":"00-00-00-00-00-12-40-0B","longitude":3.81714,"latitude":53.60341,"altitude":39},{"stationId":"00-00-00-00-00-12-40-0C","longitude":4.58833,"latitude":54.3746,"altitude":39},{"stationId":"00-00-00-00-00-12-40-0D","longitude":4.83546,"latitude":54.62173,"altitude":39},{"stationId":"00-00-00-00-00-12-40-0E","longitude":5.21795,"latitude":55.00422,"altitude":39},{"stationId":"00-00-00-00-00-12-40-0F","longitude":5.68158,"latitude":55.46785,"altitude":39},{"stationId":"00-00-00-00-00-12-40-10","longitude":5.69001,"latitude":55.47628,"altitude":39},{"stationId":"00-00-00-00-00-12-40-11","longitude":5.84261,"latitude":55.62888,"altitude":39},{"stationId":"00-00-00-00-00-12-40-12","longitude":6.64351,"latitude":56.42978,"altitude":39},{"stationId":"00-00-00-00-00-12-40-13","longitude":6.83365,"latitude":56.61992,"altitude":39},{"stationId":"00-00-00-00-00-12-40-14","longitude":7.73784,"latitude":57.52411,"altitude":39},{"stationId":"00-00-00-00-00-12-40-15","longitude":8.36238,"latitude":58.14865,"altitude":39},{"stationId":"00-00-00-00-00-12-40-16","longitude":8.63313,"latitude":58.4194,"altitude":39},{"stationId":"00-00-00-00-00-12-40-17","longitude":9.32846,"latitude":59.11473,"altitude":39},{"stationId":"00-00-00-00-00-12-40-18","longitude":10.10556,"latitude":59.89183,"altitude":39},{"stationId":"00-00-00-00-00-12-40-19","longitude":10.12372,"latitude":59.90999,"altitude":39},{"stationId":"00-00-00-00-00-12-40-1A","longitude":10.92018,"latitude":60.70645,"altitude":39},{"stationId":"00-00-00-00-00-12-40-1B","longitude":11.77842,"latitude":61.56469,"altitude":39},{"stationId":"00-00-00-00-00-12-40-1C","longitude":12.40323,"latitude":62.1895,"altitude":39},{"stationId":"00-00-00-00-00-12-40-1D","longitude":12.88216,"latitude":62.66843,"altitude":39},{"stationId":"00-00-00-00-00-12-40-1E","longitude":13.81922,"latitude":63.60549,"altitude":39},{"stationId":"00-00-00-00-00-12-40-1F","longitude":13.84097,"latitude":63.62724,"altitude":39},{"stationId":"00-00-00-00-00-12-40-20","longitude":14.24257,"latitude":64.02884,"altitude":39},{"stationId":"00-00-00-00-00-12-40-21","longitude":15.12862,"latitude":64.91489,"altitude":39},{"stationId":"00-00-00-00-00-12-40-22","longitude":15.81084,"latitude":65.59711,"altitude":39},{"stationId":"00-00-00-00-00-12-40-23","longitude":16.78342,"latitude":66.56969,"altitude":39},{"stationId":"00-00-00-00-00-12-40-24","longitude":16.92194,"latitude":66.70821,"altitude":39},{"stationId":"00-00-00-00-00-12-40-25","longitude":16.99218,"latitude":66.77845,"altitude":39},{"stationId":"00-00-00-00-00-12-40-26","longitude":17.04487,"latitude":66.83114,"altitude":39},{"stationId":"00-00-00-00-00-12-40-27","longitude":17.888,"latitude":67.67427,"altitude":39},{"stationId":"00-00-00-00-00-12-40-28","longitude":18.84738,"latitude":68.63365,"altitude":39},{"stationId":"00-00-00-00-00-12-40-29","longitude":19.36947,"latitude":69.15574,"altitude":39},{"stationId":"00-00-00-00-00-12-40-2A","longitude":19.93042,"latitude":69.71669,"altitude":39},{"stationId":"00-00-00-00-00-12-40-2B","longitude":20.37024,"latitude":70.15651,"altitude":39},{"stationId":"00-00-00-00-00-12-40-2C","longitude":20.66567,"latitude":70.45194,"altitude":39},{"stationId":"00-00-00-00-00-12-40-2D","longitude":21.11511,"latitude":70.90138,"altitude":39},{"stationId":"00-00-00-00-00-12-40-2E","longitude":21.65137,"latitude":71.43764,"altitude":39},{"stationId":"00-00-00-00-00-12-40-2F","longitude":22.03542,"latitude":71.82169,"altitude":39},{"stationId":"00-00-00-00-00-12-40-30","longitude":22.043,"latitude":71.82927,"altitude":39},{"stationId":"00-00-00-00-00-12-40-31","longitude":22.55111,"latitude":72.33738,"altitude":39},{"stationId":"00-00-00-00-00-12-40-32","longitude":23.03542,"latitude":72.82169,"altitude":39},{"stationId":"00-00-00-00-00-12-40-33","longitude":23.21843,"latitude":73.0047,"altitude":39},{"stationId":"00-00-00-00-00-12-40-34","longitude":23.42825,"latitude":73.21452,"altitude":39},{"stationId":"00-00-00-00-00-12-40-35","longitude":23.44809,"latitude":73.23436,"altitude":39},{"stationId":"00-00-00-00-00-12-40-36","longitude":23.58749,"latitude":73.37376,"altitude":39},{"stationId":"00-00-00-00-00-12-40-37","longitude":24.10464,"latitude":73.89091,"altitude":39},{"stationId":"00-00-00-00-00-12-40-38","longitude":24.64716,"latitude":74.43343,"altitude":39},{"stationId":"00-00-00-00-00-12-40-39","longitude":24.72111,"latitude":74.50738,"altitude":39},{"stationId":"00-00-00-00-00-12-40-3A","longitude":24.80107,"latitude":74.58734,"altitude":39},{"stationId":"00-00-00-00-00-12-40-3B","longitude":25.1838,"latitude":74.97007,"altitude":39},{"stationId":"00-00-00-00-00-12-40-3C","longitude":26.00034,"latitude":75.78661,"altitude":39},{"stationId":"00-00-00-00-00-12-40-3D","longitude":26.65407,"latitude":76.44034,"altitude":39},{"stationId":"00-00-00-00-00-12-40-3E","longitude":27.34099,"latitude":77.12726,"altitude":39},{"stationId":"00-00-00-00-00-12-40-3F","longitude":27.90037,"latitude":77.68664,"altitude":39},{"stationId":"00-00-00-00-00-12-40-40","longitude":28.8356,"latitude":78.62187,"altitude":39},{"stationId":"00-00-00-00-00-12-40-41","longitude":29.55536,"latitude":79.34163,"altitude":39},{"stationId":"00-00-00-00-00-12-40-42","longitude":29.68068,"latitude":79.46695,"altitude":39},{"stationId":"00-00-00-00-00-12-40-43","longitude":30.49757,"latitude":80.28384,"altitude":39},{"stationId":"00-00-00-00-00-12-40-44","longitude":31.22115,"latitude":81.00742,"altitude":39},{"stationId":"00-00-00-00-00-12-40-45","longitude":31.83665,"latitude":81.62292,"altitude":39},{"stationId":"00-00-00-00-00-12-40-46","longitude":32.39081,"latitude":82.17708,"altitude":39},{"stationId":"00-00-00-00-00-12-40-47","longitude":33.19953,"latitude":82.9858,"altitude":39},{"stationId":"00-00-00-00-00-12-40-48","longitude":33.23404,"latitude":83.02031,"altitude":39},{"stationId":"00-00-00-00-00-12-40-49","longitude":33.29108,"latitude":83.07735,"altitude":39},{"stationId":"00-00-00-00-00-12-40-4A","longitude":33.7111,"latitude":83.49737,"altitude":39},{"stationId":"00-00-00-00-00-12-40-4B","longitude":34.29116,"latitude":84.07743,"altitude":39},{"stationId":"00-00-00-00-00-12-40-4C","longitude":35.17668,"latitude":84.96295,"altitude":39},{"stationId":"00-00-00-00-00-12-40-4D","longitude":35.51972,"latitude":85.30599,"altitude":39},{"stationId":"00-00-00-00-00-12-40-4E","longitude":36.27649,"latitude":86.06276,"altitude":39},{"stationId":"00-00-00-00-00-12-40-4F","longitude":36.55872,"latitude":86.34499,"altitude":39},{"stationId":"00-00-00-00-00-12-40-50","longitude":37.04356,"latitude":86.82983,"altitude":39},{"stationId":"00-00-00-00-00-12-40-51","longitude":38.02396,"latitude":87.81023,"altitude":39},{"stationId":"00-00-00-00-00-12-40-52","longitude":38.32183,"latitude":88.1081,"altitude":39},{"stationId":"00-00-00-00-00-12-40-53","longitude":38.78512,"latitude":88.57139,"altitude":39},{"stationId":"00-00-00-00-00-12-40-54","longitude":39.48792,"latitude":89.27419,"altitude":39},{"stationId":"00-00-00-00-00-12-40-55","longitude":40.3248,"latitude":90.11107,"altitude":39},{"stationId":"00-00-00-00-00-12-40-56","longitude":40.36967,"latitude":90.15594,"altitude":39},{"stationId":"00-00-00-00-00-12-40-57","longitude":40.75025,"latitude":90.53652,"altitude":39},{"stationId":"00-00-00-00-00-12-40-58","longitude":41.53673,"latitude":91.323,"altitude":39},{"stationId":"00-00-00-00-00-12-40-59","longitude":41.75628,"latitude":91.54255,"altitude":39},{"stationId":"00-00-00-00-00-12-40-5A","longitude":42.12122,"latitude":91.90749,"altitude":39},{"stationId":"00-00-00-00-00-12-40-5B","longitude":42.67528,"latitude":92.46155,"altitude":39},{"stationId":"00-00-00-00-00-12-40-5C","longitude":43.15264,"latitude":92.93891,"altitude":39},{"stationId":"00-00-00-00-00-12-40-5D","longitude":43.34434,"latitude":93.13061,"altitude":39},{"stationId":"00-00-00-00-00-12-40-5E","longitude":44.30806,"latitude":94.09433,"altitude":39},{"stationId":"00-00-00-00-00-12-40-5F","longitude":45.30253,"latitude":95.0888,"altitude":39},{"stationId":"00-00-00-00-00-12-40-60","longitude":45.48519,"latitude":95.27146,"altitude":39},{"stationId":"00-00-00-00-00-12-40-61","longitude":45.62806,"latitude":95.41433,"altitude":39},{"stationId":"00-00-00-00-00-12-40-62","longitude":45.91029,"latitude":95.69656,"altitude":39},{"stationId":"00-00-00-00-00-12-40-63","longitude":46.56298,"latitude":96.34925,"altitude":39},{"stationId":"00-7F-FF-FF-FE-01-02-03"},{"stationId":"00-00-00-00-00-00-0A-AA"},{"stationId":"00-00-00-00-00-00-0B-BB"},{"stationId":"00-00-00-00-00-00-AA-01"},{"stationId":"00-00-00-00-00-00-AA-02"},{"stationId":"00-00-00-AA-BB-CC-DD-EE"},{"stationId":"00-00-00-00-08-06-05-05"},{"stationId":"00-00-00-00-0B-03-00-4D"},{"stationId":"72-76-FF-00-08-06-05-05"},{"stationId":"72-76-FF-00-08-06-05-07"},{"stationId":"72-76-FF-00-08-06-05-09"},{"stationId":"00-00-00-00-00-00-00-0F"},{"stationId":"00-00-00-00-00-00-00-F2"},{"stationId":"00-00-00-00-00-00-00-01"},{"stationId":"00-00-00-00-00-00-00-02"},{"stationId":"00-00-00-00-00-00-00-00"},{"stationId":"00-00-00-00-00-00-00-91"},{"stationId":"72-76-FF-FF-FE-01-06-48"},{"stationId":"AA-55-5A-00-00-00-00-00"},{"stationId":"72-76-FF-FF-FE-01-06-4B"},{"stationId":"00-00-00-00-00-00-45-45"},{"stationId":"00-00-00-00-AA-BB-CC-01"},{"stationId":"00-00-00-00-AA-BB-CC-DD"},{"stationId":"00-00-00-0A-AA-AA-AA-AA"},{"stationId":"00-00-00-00-00-AA-11-BB"},{"stationId":"00-00-00-00-00-AA-22-BB"},{"stationId":"00-00-00-00-00-AA-33-BB"},{"stationId":"00-00-00-00-00-AA-55-BB"}]}
							</body>
							<headers>
								<header name="Transfer-Encoding" value="chunked" />
								<header name="X-Frame-Options" value="DENY" />
								<header name="Cache-Control"
									value="no-cache, no-store, max-age=0, must-revalidate" />
								<header name="X-Content-Type-Options" value="nosniff" />
								<header name="Expires" value="0" />
								<header name="Pragma" value="no-cache" />
								<header name="X-XSS-Protection" value="1; mode=block" />
								<header name="X-Application-Context"
									value="application:rec" />
								<header name="Date" value="Tue, 13 Dec 2016 08:13:28 GMT" />
								<header name="Content-Type"
									value="application/vnd.kerlink.lns-v1+json;charset=UTF-8" />
							</headers>
						</response>
					</exchange>
				</exchanges>
			</endpoint>
			<endpoint name="createStation" path="/application/stations"
				method="PUT">
				<exchanges>
					<exchange name="echange" date="1481560072782" status="0">
						<request
							uri="http://149.202.168.118:45800/application/stations?stationId=toto">
							<body />
							<parameters>
								<parameter enabled="true" location="HEADER"
									name="Authorization"
									value="Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlcm9zcyIsInJvbGUiOiJTVVBFUl9BRE1JTiIsImdyb3VwSWQiOiI2IiwiaXNzIjoib3NzQ2xpZW50IiwiZXhwIjoxNDgxNjUyNTY4fQ.zcHZz7sNEEqIDJ6UyMAcBkBaZCH8CmWdp0BGAuM7OMQ" />
								<parameter enabled="true" location="QUERY"
									name="stationId" value="toto" />
							</parameters>
						</request>
						<response status="0">
							<body />
							<headers />
						</response>
					</exchange>
				</exchanges>
			</endpoint>
			<path name="{stationId}">
				<endpoint name="getStation"
					path="/application/stations/{stationId}" method="GET">
					<exchanges>
						<exchange name="echange" date="1481560423990"
							status="200">
							<request
								uri="http://149.202.168.118:45800/application/stations/AA-66-BB">
								<body />
								<parameters>
									<parameter enabled="true" location="PATH"
										name="stationId" value="AA-66-BB" />
									<parameter enabled="true" location="HEADER"
										name="Authorization"
										value="Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlcm9zcyIsInJvbGUiOiJTVVBFUl9BRE1JTiIsImdyb3VwSWQiOiI2IiwiaXNzIjoib3NzQ2xpZW50IiwiZXhwIjoxNDgxNjUyNTY4fQ.zcHZz7sNEEqIDJ6UyMAcBkBaZCH8CmWdp0BGAuM7OMQ" />
								</parameters>
							</request>
							<response status="200">
								<body>{"stationId":"00-00-00-00-00-AA-66-BB"}</body>
								<headers>
									<header name="Transfer-Encoding" value="chunked" />
									<header name="X-Frame-Options" value="DENY" />
									<header name="Cache-Control"
										value="no-cache, no-store, max-age=0, must-revalidate" />
									<header name="X-Content-Type-Options" value="nosniff" />
									<header name="Expires" value="0" />
									<header name="Pragma" value="no-cache" />
									<header name="X-XSS-Protection" value="1; mode=block" />
									<header name="X-Application-Context"
										value="application:rec" />
									<header name="Date"
										value="Mon, 12 Dec 2016 16:34:20 GMT" />
									<header name="Content-Type"
										value="application/vnd.kerlink.lns-v1+json;charset=UTF-8" />
								</headers>
							</response>
						</exchange>
					</exchanges>
				</endpoint>
			</path>
		</path>
		<path name="clusters">
			<endpoint name="createCluster" path="/application/clusters"
				method="POST">
				<exchanges>
					<exchange name="echange" date="1481532695795" status="400">
						<request
							uri="http://149.202.168.118:45800/application/clusters">
							<body />
							<parameters>
								<parameter enabled="true" location="HEADER"
									name="Authorization"
									value="Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlcm9zcyIsInJvbGUiOiJTVVBFUl9BRE1JTiIsImdyb3VwSWQiOiI2IiwiaXNzIjoib3NzQ2xpZW50IiwiZXhwIjoxNDgxNjUyNTY4fQ.zcHZz7sNEEqIDJ6UyMAcBkBaZCH8CmWdp0BGAuM7OMQ" />
							</parameters>
						</request>
						<response status="400">
							<body>{"code":"BAD_REQUEST","message":"parameter name can’t be
								null or empty"}</body>
							<headers>
								<header name="Transfer-Encoding" value="chunked" />
								<header name="X-Frame-Options" value="DENY" />
								<header name="Cache-Control"
									value="no-cache, no-store, max-age=0, must-revalidate" />
								<header name="X-Content-Type-Options" value="nosniff" />
								<header name="Connection" value="close" />
								<header name="Expires" value="0" />
								<header name="Pragma" value="no-cache" />
								<header name="X-XSS-Protection" value="1; mode=block" />
								<header name="X-Application-Context"
									value="application:rec" />
								<header name="Date" value="Mon, 12 Dec 2016 08:52:10 GMT" />
								<header name="Content-Type"
									value="application/vnd.kerlink.lns-v1+json;charset=UTF-8" />
							</headers>
						</response>
					</exchange>
				</exchanges>
			</endpoint>
			<endpoint name="getClusters" path="/application/clusters"
				method="GET">
				<exchanges>
					<exchange name="echange" date="1481531717300" status="200">
						<request
							uri="http://149.202.168.118:45800/application/clusters?pageSize=200&amp;pageNumber=1">
							<body />
							<parameters>
								<parameter enabled="true" location="HEADER"
									name="Authorization"
									value="Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlcm9zcyIsInJvbGUiOiJTVVBFUl9BRE1JTiIsImdyb3VwSWQiOiI2IiwiaXNzIjoib3NzQ2xpZW50IiwiZXhwIjoxNDgxNjUyNTY4fQ.zcHZz7sNEEqIDJ6UyMAcBkBaZCH8CmWdp0BGAuM7OMQ" />
								<parameter enabled="true" location="QUERY"
									name="pageNumber" value="1" />
								<parameter enabled="true" location="QUERY"
									name="pageSize" value="200" />
							</parameters>
						</request>
						<response status="200">
							<body>{"page":1,"nbPages":1,"pageSize":200,"count":22,"totalCount":22,"dtos":[{"id":1,"groupId":"1","name":"cluster_name","decryptPayload":true,"pushUrl":"pushurl","pushUser":"pushuser","accessType":"REST","msgDetailLevel":"PAYLOAD","geolocEnabled":true,"geolocAlgorithm":[],"tkmManagement":false,"tkmUrl":"tkmurl","tkmUser":"tkmuser"},{"id":2,"groupId":"1","name":"grappe1","decryptPayload":true,"pushUrl":"http://localhost:8888","pushUser":"user","accessType":"REST","msgDetailLevel":"PAYLOAD","geolocEnabled":true,"geolocAlgorithm":[],"tkmManagement":true,"tkmUrl":"http://localhost:8888","tkmUser":"user"},{"id":5,"groupId":"6","name":"cluster_OMA01","decryptPayload":true,"pushUrl":"","pushUser":"","accessType":"","msgDetailLevel":"","geolocEnabled":true,"geolocAlgorithm":["Kerlink"],"tkmManagement":true,"tkmUrl":"","tkmUser":""},{"id":7,"groupId":"6","name":"cluster_OMA02","decryptPayload":true,"pushUrl":"","pushUser":"","accessType":"","msgDetailLevel":"","geolocEnabled":true,"geolocAlgorithm":["Kerlink"],"tkmManagement":true,"tkmUrl":"","tkmUser":""},{"id":8,"groupId":"6","name":"CL
								MT
								01","decryptPayload":false,"pushUrl":"test","pushUser":"test","accessType":"WEBSOCKET","msgDetailLevel":"","geolocEnabled":true,"geolocAlgorithm":["RSSI"],"tkmManagement":true,"tkmUrl":"sss","tkmUser":"sfsfd"},{"id":10,"groupId":"","name":"CL
								MT
								02","decryptPayload":false,"pushUrl":"test","pushUser":"test","accessType":"WEBSOCKET","msgDetailLevel":"","geolocEnabled":true,"geolocAlgorithm":["Kerlink"],"tkmManagement":false,"tkmUrl":"","tkmUser":""},{"id":11,"groupId":"5","name":"CL
								MT
								03","decryptPayload":false,"pushUrl":"fqfdsf","pushUser":"fqsf","accessType":"REST","msgDetailLevel":"","geolocEnabled":true,"geolocAlgorithm":["kerlink"],"tkmManagement":false,"tkmUrl":"","tkmUser":""},{"id":12,"groupId":"5","name":"CL
								MT
								04","decryptPayload":false,"pushUrl":"qsdfqs","pushUser":"fqsfsq","accessType":"WEBSOCKET","msgDetailLevel":"","geolocEnabled":true,"geolocAlgorithm":["rssi","kerlink"],"tkmManagement":false,"tkmUrl":"","tkmUser":""},{"id":13,"groupId":"5","name":"CL
								MT
								05","decryptPayload":false,"pushUrl":"sfqsd","pushUser":"fqfqsd","accessType":"WEBSOCKET","msgDetailLevel":"","geolocEnabled":true,"geolocAlgorithm":["rssi"],"tkmManagement":false,"tkmUrl":"","tkmUser":""},{"id":17,"groupId":"6","name":"CL
								MT
								06","decryptPayload":false,"pushUrl":"fsdff","pushUser":"fdsf","accessType":"WEBSOCKET","msgDetailLevel":"","geolocEnabled":true,"geolocAlgorithm":["kerlink"],"tkmManagement":false,"tkmUrl":"","tkmUser":""},{"id":18,"groupId":"6","name":"CL
								MT
								07","decryptPayload":false,"pushUrl":"fqdsf","pushUser":"qsfdqs","accessType":"WEBSOCKET","msgDetailLevel":"","geolocEnabled":true,"geolocAlgorithm":["kerlink"],"tkmManagement":false,"tkmUrl":"","tkmUser":""},{"id":19,"groupId":"6","name":"CL
								MT
								08","decryptPayload":false,"pushUrl":"fqsdf","pushUser":"dsqfqsdf","accessType":"WEBSOCKET","msgDetailLevel":"","geolocEnabled":true,"geolocAlgorithm":["kerlink"],"tkmManagement":false,"tkmUrl":"","tkmUser":""},{"id":21,"groupId":"1","name":"grappeBlabla","decryptPayload":true,"pushUrl":"http://localhost:8888","pushUser":"user","accessType":"REST","msgDetailLevel":"PAYLOAD","geolocEnabled":true,"geolocAlgorithm":["RSSI"],"tkmManagement":true,"tkmUrl":"http://localhost:8888","tkmUser":"user"},{"id":27,"groupId":"6","name":"CL
								MT
								09","decryptPayload":false,"pushUrl":"test","pushUser":"test","accessType":"WEBSOCKET","msgDetailLevel":"","geolocEnabled":true,"geolocAlgorithm":["OTA"],"tkmManagement":false,"tkmUrl":"","tkmUser":""},{"id":28,"groupId":"6","name":"CL
								MT
								10","decryptPayload":false,"pushUrl":"test","pushUser":"test","accessType":"WEBSOCKET","msgDetailLevel":"","geolocEnabled":true,"geolocAlgorithm":["RSSI","Kerlink"],"tkmManagement":false,"tkmUrl":"","tkmUser":""},{"id":31,"groupId":"6","name":"Cluster_OMA2","decryptPayload":false,"pushUrl":"http://monserveur.com","pushUser":"toto","accessType":"REST","msgDetailLevel":"NETWORK","geolocEnabled":false,"geolocAlgorithm":["RSSI"],"tkmManagement":false,"tkmUrl":"http://monserveur.com","tkmUser":"aa"},{"id":32,"groupId":"6","name":"cccc","decryptPayload":false,"pushUrl":"test","pushUser":"test","accessType":"WEBSOCKET","msgDetailLevel":"PAYLOAD","geolocEnabled":true,"geolocAlgorithm":["RSSI"],"tkmManagement":true,"tkmUrl":"test","tkmUser":"test"},{"id":33,"groupId":"6","name":"cluster
								orange","decryptPayload":false,"pushUrl":"http://push.com","pushUser":"user","accessType":"REST","msgDetailLevel":"PAYLOAD","geolocEnabled":true,"geolocAlgorithm":["RSSI","Kerlink"],"tkmManagement":false,"tkmUrl":"","tkmUser":""},{"id":34,"name":"grappeTESTTTTTT","decryptPayload":true,"pushUrl":"http://localhost:8888","pushUser":"user","accessType":"REST","msgDetailLevel":"PAYLOAD","geolocEnabled":true,"geolocAlgorithm":["RSSI"],"tkmManagement":true,"tkmUrl":"http://localhost:8888","tkmUser":"user"},{"id":35,"name":"TEST
								ADMIN
								01","decryptPayload":false,"pushUrl":"test","pushUser":"test","accessType":"REST","msgDetailLevel":"NETWORK","geolocEnabled":true,"geolocAlgorithm":["RSSI"],"tkmManagement":false,"tkmUrl":"","tkmUser":""},{"id":36,"name":"TEST
								ADMIN
								02","decryptPayload":false,"pushUrl":"test","pushUser":"test","accessType":"REST","msgDetailLevel":"NETWORK","geolocEnabled":true,"geolocAlgorithm":["RSSI","OTA"],"tkmManagement":false,"tkmUrl":"","tkmUser":""},{"id":37,"groupId":"6","name":"cluster_validation","decryptPayload":false,"pushUrl":"test","pushUser":"test","accessType":"WEBSOCKET","msgDetailLevel":"PAYLOAD","geolocEnabled":true,"geolocAlgorithm":["Kerlink"],"tkmManagement":false,"tkmUrl":"","tkmUser":""}]}
							</body>
							<headers>
								<header name="Transfer-Encoding" value="chunked" />
								<header name="X-Frame-Options" value="DENY" />
								<header name="Cache-Control"
									value="no-cache, no-store, max-age=0, must-revalidate" />
								<header name="X-Content-Type-Options" value="nosniff" />
								<header name="Expires" value="0" />
								<header name="Pragma" value="no-cache" />
								<header name="X-XSS-Protection" value="1; mode=block" />
								<header name="X-Application-Context"
									value="application:rec" />
								<header name="Date" value="Mon, 12 Dec 2016 08:35:52 GMT" />
								<header name="Content-Type"
									value="application/vnd.kerlink.lns-v1+json;charset=UTF-8" />
							</headers>
						</response>
					</exchange>
				</exchanges>
			</endpoint>
		</path>
		<path name="endpoints">
			<endpoint name="getEndpoints" path="/application/endpoints"
				method="GET">
				<exchanges>
					<exchange name="echange" date="1481617195395" status="200">
						<request
							uri="http://149.202.168.118:45800/application/endpoints?pageSize=50&amp;pageNumber=1">
							<body />
							<parameters>
								<parameter enabled="true" location="HEADER"
									name="Authorization"
									value="Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlcm9zcyIsInJvbGUiOiJTVVBFUl9BRE1JTiIsImdyb3VwSWQiOiI2IiwiaXNzIjoib3NzQ2xpZW50IiwiZXhwIjoxNDgxOTMxODEyfQ.eYFXVujPhohsBrVXh5hr8kBkTKFViyYVtOKGnsCgHi8" />
								<parameter enabled="true" location="QUERY"
									name="pageNumber" value="1" />
								<parameter enabled="true" location="QUERY"
									name="pageSize" value="50" />
								<parameter enabled="false" location="QUERY"
									name="fields" value="devEui" />
							</parameters>
						</request>
						<response status="200">
							<body>{"page":1,"nbPages":11,"pageSize":50,"count":50,"totalCount":517,"dtos":[{"devEui":"12-00-00-00-00-00-00-00","devAddr":"12-00-00-00","groupId":"1","fcntDown":1069,"status":"FCOUNT_ERROR","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.1156,"altitude":10,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastTXMessageTimestamp":1481590734785,"lastRXMessageTimestamp":1481590691000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-01","devAddr":"12-00-00-01","groupId":"1","fcntDown":1024,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1481590702000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-02","devAddr":"12-00-00-02","groupId":"1","fcntDown":1024,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1481590700000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-03","devAddr":"12-00-00-03","groupId":"1","fcntDown":1023,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1481590686000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-04","devAddr":"12-00-00-04","groupId":"1","fcntDown":1024,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastTXMessageTimestamp":1480080405920,"lastRXMessageTimestamp":1481590698000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-05","devAddr":"12-00-00-05","groupId":"1","fcntDown":1024,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1481590690000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-06","devAddr":"12-00-00-06","groupId":"1","fcntDown":1023,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1481590695000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-07","devAddr":"12-00-00-07","groupId":"1","fcntDown":0,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1481590687000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-08","devAddr":"12-00-00-08","groupId":"1","fcntDown":1023,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1481590686000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-09","devAddr":"12-00-00-09","groupId":"1","fcntDown":1023,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1481590686000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-0A","devAddr":"12-00-00-0A","groupId":"1","fcntDown":2,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775984000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-0B","devAddr":"12-00-00-0B","groupId":"1","fcntDown":3,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775984000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-0C","devAddr":"12-00-00-0C","groupId":"1","fcntDown":2,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-0D","devAddr":"12-00-00-0D","groupId":"1","fcntDown":8,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-0E","devAddr":"12-00-00-0E","groupId":"1","fcntDown":1,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775984000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-0F","devAddr":"12-00-00-0F","groupId":"1","fcntDown":1,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775984000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-10","devAddr":"12-00-00-10","groupId":"1","fcntDown":1,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775987000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-11","devAddr":"12-00-00-11","groupId":"1","fcntDown":7,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-12","devAddr":"12-00-00-12","groupId":"1","fcntDown":1,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-13","devAddr":"12-00-00-13","groupId":"1","fcntDown":8,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-14","devAddr":"12-00-00-14","groupId":"1","fcntDown":1,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775984000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-15","devAddr":"12-00-00-15","groupId":"1","fcntDown":3,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-16","devAddr":"12-00-00-16","groupId":"1","fcntDown":1,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-17","devAddr":"12-00-00-17","groupId":"1","fcntDown":6,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.5,"altitude":20,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-18","devAddr":"12-00-00-18","groupId":"1","fcntDown":58,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775987000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-19","devAddr":"12-00-00-19","groupId":"1","fcntDown":20,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-1A","devAddr":"12-00-00-1A","groupId":"1","fcntDown":11,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-1B","devAddr":"12-00-00-1B","groupId":"1","fcntDown":5,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-1C","devAddr":"12-00-00-1C","groupId":"1","fcntDown":35,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775988000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-1D","devAddr":"12-00-00-1D","groupId":"1","fcntDown":6,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775987000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-1E","devAddr":"12-00-00-1E","groupId":"1","fcntDown":9,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775987000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-1F","devAddr":"12-00-00-1F","groupId":"1","fcntDown":136,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-20","devAddr":"12-00-00-20","groupId":"1","fcntDown":1,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-21","devAddr":"12-00-00-21","groupId":"1","fcntDown":1,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-22","devAddr":"12-00-00-22","groupId":"1","fcntDown":1,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775986000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-23","devAddr":"12-00-00-23","groupId":"1","fcntDown":20,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-24","devAddr":"12-00-00-24","groupId":"1","fcntDown":3,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-25","devAddr":"12-00-00-25","groupId":"1","fcntDown":1,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-26","devAddr":"12-00-00-26","groupId":"1","fcntDown":4,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-27","devAddr":"12-00-00-27","groupId":"1","fcntDown":18,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-28","devAddr":"12-00-00-28","groupId":"1","fcntDown":3,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775988000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-29","devAddr":"12-00-00-29","groupId":"1","fcntDown":5,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775984000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-2A","devAddr":"12-00-00-2A","groupId":"1","fcntDown":3,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775987000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-2B","devAddr":"12-00-00-2B","groupId":"1","fcntDown":3,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775987000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-2C","devAddr":"12-00-00-2C","groupId":"1","fcntDown":31,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775986000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-2D","devAddr":"12-00-00-2D","groupId":"1","fcntDown":25,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775986000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-2E","devAddr":"12-00-00-2E","groupId":"1","fcntDown":1,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-2F","devAddr":"12-00-00-2F","groupId":"1","fcntDown":6,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775987000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-30","devAddr":"12-00-00-30","groupId":"1","fcntDown":9,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775986000,"classType":"C"},{"devEui":"12-00-00-00-00-00-00-31","devAddr":"12-00-00-31","groupId":"1","fcntDown":25,"status":"UPLINK_OK","clusterId":1,"profile":"STATIC_INDOOR","geolocation":"MANUAL","longitude":-1.67429,"latitude":48.11198,"altitude":39,"nwkSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appSKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","appKey":"aa:bb:cc:dd:ee:ff:00:11:22:33:44:55:66:77:88:99","activation":"ABP","rx1Delay":1,"rx2Sf":0,"rx2Freq":0,"rxWindows":"AUTO","region":"europe863","lastRXMessageTimestamp":1478775985000,"classType":"C"}]}
							</body>
							<headers>
								<header name="Transfer-Encoding" value="chunked" />
								<header name="X-Frame-Options" value="DENY" />
								<header name="Cache-Control"
									value="no-cache, no-store, max-age=0, must-revalidate" />
								<header name="X-Content-Type-Options" value="nosniff" />
								<header name="Expires" value="0" />
								<header name="Pragma" value="no-cache" />
								<header name="X-XSS-Protection" value="1; mode=block" />
								<header name="X-Application-Context"
									value="application:rec" />
								<header name="Date" value="Tue, 13 Dec 2016 08:20:31 GMT" />
								<header name="Content-Type"
									value="application/vnd.kerlink.lns-v1+json;charset=UTF-8" />
							</headers>
						</response>
					</exchange>
				</exchanges>
			</endpoint>
			<path name="{devEUI}">
				<path name="stations">
					<endpoint name="getStationsWhoSawEndpoint"
						path="/application/endpoints/{devEUI}/stations" method="GET">
						<exchanges>
							<exchange name="echange" date="1481617219124"
								status="200">
								<request
									uri="http://149.202.168.118:45800/application/endpoints/12-00-00-00-00-00-00-01/stations?pageSize=50&amp;startDate=123456&amp;sort=-lastDate&amp;pageNumber=1">
									<body />
									<parameters>
										<parameter enabled="true" location="PATH"
											name="devEUI" value="12-00-00-00-00-00-00-01" />
										<parameter enabled="true" location="HEADER"
											name="Authorization"
											value="Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlcm9zcyIsInJvbGUiOiJTVVBFUl9BRE1JTiIsImdyb3VwSWQiOiI2IiwiaXNzIjoib3NzQ2xpZW50IiwiZXhwIjoxNDgxNjUyNTY4fQ.zcHZz7sNEEqIDJ6UyMAcBkBaZCH8CmWdp0BGAuM7OMQ" />
										<parameter enabled="true" location="QUERY"
											name="pageNumber" value="1" />
										<parameter enabled="true" location="QUERY"
											name="pageSize" value="50" />
										<parameter enabled="true" location="QUERY"
											name="startDate" value="123456" />
										<parameter enabled="true" location="QUERY"
											name="sort" value="-lastDate" />
									</parameters>
								</request>
								<response status="200">
									<body>{"page":1,"nbPages":1,"pageSize":50,"count":10,"totalCount":10,"dtos":[{"lastDate":1481590702000,"station":{"stationId":"00-00-00-00-00-12-40-00","longitude":-1.67429,"latitude":48.11198,"altitude":39}},{"lastDate":1481504293000,"station":{"stationId":"00-00-00-00-00-12-40-07","longitude":2.19838,"latitude":51.98465,"altitude":39}},{"lastDate":1481504293000,"station":{"stationId":"00-00-00-00-00-12-40-01","longitude":-0.80422,"latitude":48.98205,"altitude":39}},{"lastDate":1481504293000,"station":{"stationId":"00-00-00-00-00-12-40-04","longitude":0.72246,"latitude":50.50873,"altitude":39}},{"lastDate":1481504293000,"station":{"stationId":"00-00-00-00-00-12-40-08","longitude":2.67071,"latitude":52.45698,"altitude":39}},{"lastDate":1481504293000,"station":{"stationId":"00-00-00-00-00-12-40-03","longitude":-0.0192,"latitude":49.76707,"altitude":39}},{"lastDate":1481504293000,"station":{"stationId":"00-00-00-00-00-12-40-09","longitude":2.983,"latitude":52.76927,"altitude":39}},{"lastDate":1481331505000,"station":{"stationId":"00-00-00-00-00-12-40-05","longitude":1.33159,"latitude":51.11786,"altitude":39}},{"lastDate":1481331505000,"station":{"stationId":"00-00-00-00-00-12-40-06","longitude":1.49235,"latitude":51.27862,"altitude":39}},{"lastDate":1481245094000,"station":{"stationId":"00-00-00-00-00-12-40-02","longitude":-0.14086,"latitude":49.64541,"altitude":39}}]}
									</body>
									<headers>
										<header name="Transfer-Encoding" value="chunked" />
										<header name="X-Frame-Options" value="DENY" />
										<header name="Cache-Control"
											value="no-cache, no-store, max-age=0, must-revalidate" />
										<header name="X-Content-Type-Options" value="nosniff" />
										<header name="Expires" value="0" />
										<header name="Pragma" value="no-cache" />
										<header name="X-XSS-Protection" value="1; mode=block" />
										<header name="X-Application-Context"
											value="application:rec" />
										<header name="Date"
											value="Tue, 13 Dec 2016 08:20:54 GMT" />
										<header name="Content-Type"
											value="application/vnd.kerlink.lns-v1+json;charset=UTF-8" />
									</headers>
								</response>
							</exchange>
						</exchanges>
					</endpoint>
				</path>
				<path name="txmessages">
					<endpoint name="sendTXMessage"
						path="/application/endpoints/{devEUI}/txmessages" method="POST">
						<exchanges>
							<exchange name="echange" date="1481532966820" status="0">
								<request
									uri="http://149.202.168.118:45800/application/endpoints/12-00-00-00-00-00-00-00/txmessages">
									<body />
									<parameters>
										<parameter enabled="true" location="PATH"
											name="devEUI" value="12-00-00-00-00-00-00-00" />
									</parameters>
								</request>
								<response status="0">
									<body />
									<headers />
								</response>
							</exchange>
						</exchanges>
					</endpoint>
				</path>
			</path>
		</path>
		<endpoint name="getVersion" path="/application" method="GET">
			<exchanges>
				<exchange name="echange" date="1481531748623" status="200">
					<request uri="http://149.202.168.118:45800/application">
						<body />
						<parameters />
					</request>
					<response status="200">
						<body>{"version":"0.9.5-43-g84f7890"}</body>
						<headers>
							<header name="Transfer-Encoding" value="chunked" />
							<header name="X-Frame-Options" value="DENY" />
							<header name="Cache-Control"
								value="no-cache, no-store, max-age=0, must-revalidate" />
							<header name="X-Content-Type-Options" value="nosniff" />
							<header name="Expires" value="0" />
							<header name="Pragma" value="no-cache" />
							<header name="X-XSS-Protection" value="1; mode=block" />
							<header name="X-Application-Context"
								value="application:rec" />
							<header name="Date" value="Mon, 12 Dec 2016 08:36:23 GMT" />
							<header name="Content-Type"
								value="application/vnd.kerlink.lns-v1+json;charset=UTF-8" />
						</headers>
					</response>
				</exchange>
			</exchanges>
		</endpoint>
	</path>
</project>
