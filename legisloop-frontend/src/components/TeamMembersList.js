import React, { useEffect, useState } from "react";

function TeamMembersList() {
    const [members, setMembers] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch('http://localhost:8080/users');
                const data = await response.json();
                console.log('Balls', data);
                setMembers(data);
            } catch (error) {
                console.error("Failed to fetch members:", error);
            }
        };

        fetchData(); // Call the async function
    }, [])

    return (
        <div>
            <ul>
                {members.map((member) => (
                    <li key={member.id} className="underline">
                        {member.name}
                    </li>
                ))}
            </ul>
        </div>
    );
}
export default TeamMembersList;