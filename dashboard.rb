# https://randomuser.me/api/?inc=name,picture,location,dob&noinfo

require 'json'
require "net/https"
require "uri"

count = 15
clients = []
Struct.new("Client", :_class, :givenName, :familyName, :address, :imageUrl, :birthday)

count.times do |client|
    uri = URI.parse("https://randomuser.me/api/?inc=name,picture,location,dob&noinfo")
    http = Net::HTTP.new(uri.host, uri.port)
    http.use_ssl = true
    request = Net::HTTP::Get.new(uri.request_uri)

    response = http.request(request)
    body = JSON.parse(response.body)

    klass_name  = "io.pivotal.samples.dashboard.domain.Client"
    givenName   = body["results"].first["name"]["first"].capitalize
    familyName  = body["results"].first["name"]["last"].capitalize
    street      = body["results"].first["location"]["street"].capitalize
    city        = body["results"].first["location"]["city"].capitalize
    address     = "#{street}, #{city}"
    dob         = body["results"].first["dob"]
    image       = body["results"].first["picture"]["large"]

    clients << Struct::Client.new(klass_name, givenName, familyName, address, image, dob).to_h
end

File.write("#{__dir__}/src/main/resources/clients.json", clients.to_json)