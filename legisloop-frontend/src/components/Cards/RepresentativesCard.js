import { useNavigate } from "react-router-dom";

export default function RepresentativeGrid() {
    const representatives = [
        {
            name: "John Doe",
            title: "Senator",
            service: "Serving since 2015",
            topics: "Healthcare, Education",
        },
        {
            name: "Jane Smith",
            title: "Governor",
            service: "Serving since 2018",
            topics: "Economy, Environment",
        },
        {
            name: "Alice Johnson",
            title: "Mayor",
            service: "Serving since 2020",
            topics: "Infrastructure, Safety",
        },
        {
            name: "Robert Brown",
            title: "Congressman",
            service: "Serving since 2017",
            topics: "Technology, Jobs",
        },
        {
            name: "Robert Brown",
            title: "Congressman",
            service: "Serving since 2017",
            topics: "Technology, Jobs",
        },
        {
            name: "Robert Brown",
            title: "Congressman",
            service: "Serving since 2017",
            topics: "Technology, Jobs",
        },
        {
            name: "Robert Brown",
            title: "Congressman",
            service: "Serving since 2017",
            topics: "Technology, Jobs",
        },
        {
            name: "Robert Brown",
            title: "Congressman",
            service: "Serving since 2017",
            topics: "Technology, Jobs",
        },
        {
            name: "Robert Brown",
            title: "Congressman",
            service: "Serving since 2017",
            topics: "Technology, Jobs",
        },
        {
            name: "Robert Brown",
            title: "Congressman",
            service: "Serving since 2017",
            topics: "Technology, Jobs",
        },
        {
            name: "Robert Brown",
            title: "Congressman",
            service: "Serving since 2017",
            topics: "Technology, Jobs",
        },{
            name: "Robert Brown",
            title: "Congressman",
            service: "Serving since 2017",
            topics: "Technology, Jobs",
        },{
            name: "Robert Brown",
            title: "Congressman",
            service: "Serving since 2017",
            topics: "Technology, Jobs",
        },{
            name: "Robert Brown",
            title: "Congressman",
            service: "Serving since 2017",
            topics: "Technology, Jobs",
        },



    ];

    return (
        <div className="h-screen overflow-y-auto p-4 bg-orange-50">
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                {representatives.map((rep, index) => (
                    <RepresentativeCard key={index} {...rep} />
                ))}
            </div>
        </div>
    );
}

function RepresentativeCard({ name, title, service, topics, phoneNumber, email }) {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(`/representative/${name.replace(/\s+/g, "-").toLowerCase()}`, {
            state: { name, title, service, topics, phoneNumber, email },
        });
    };

    return (
        <div
            className="bg-white rounded-xl shadow-md p-4 border border-gray-200 hover:shadow-lg transition-shadow cursor-pointer"
            onClick={handleClick}
        >
            <div className="h-32 bg-gray-200 rounded-md mb-4 flex items-center justify-center">
                <span className="text-gray-400">Image Placeholder</span>
            </div>
            <h2 className="text-lg font-semibold">{name}</h2>
            <p className="text-gray-600 font-bold">{title}</p>
            <p className="text-sm text-gray-500">{service}</p>
            <p className="text-sm text-gray-500">{topics}</p>
        </div>
    );
}
